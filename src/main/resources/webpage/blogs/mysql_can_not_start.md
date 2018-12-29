# Mysql Fail Recorder

Recently, I always get up and find my website's tool, rate tool, is down. I can get JSON object but only get 0 value.
After log in to my server, I find my Mysql server is down. it shows 
```bash
root@HTTP_Server:~# mysql
ERROR 2002 (HY000): Can't connect to local MySQL server through socket '/var/run/mysqld/mysqld.sock' (2)
```

It looks like I can not connect to my server.

Using follow command it says
```bash
root@HTTP_Server:~# /etc/init.d/mysql status
● mysql.service - MySQL Community Server
   Loaded: loaded (/lib/systemd/system/mysql.service; enabled; vendor preset: enabled)
   Active: failed (Result: exit-code) since Sat 2018-12-29 15:44:56 UTC; 2h 31min ago
  Process: 26734 ExecStart=/usr/sbin/mysqld --daemonize --pid-file=/run/mysqld/mysqld.pid (code=exited, status=1/FAILURE)
  Process: 26705 ExecStartPre=/usr/share/mysql/mysql-systemd-start pre (code=exited, status=0/SUCCESS)
 Main PID: 4318 (code=killed, signal=KILL)

Dec 29 15:44:56 HTTP_Server systemd[1]: mysql.service: Failed with result 'exit-code'.
Dec 29 15:44:56 HTTP_Server systemd[1]: Failed to start MySQL Community Server.
Dec 29 15:44:56 HTTP_Server systemd[1]: mysql.service: Service RestartSec=100ms expired, scheduling restart.
Dec 29 15:44:56 HTTP_Server systemd[1]: mysql.service: Scheduled restart job, restart counter is at 6.
Dec 29 15:44:56 HTTP_Server systemd[1]: Stopped MySQL Community Server.
Dec 29 15:44:56 HTTP_Server systemd[1]: mysql.service: Start request repeated too quickly.
Dec 29 15:44:56 HTTP_Server systemd[1]: mysql.service: Failed with result 'exit-code'.
Dec 29 15:44:56 HTTP_Server systemd[1]: Failed to start MySQL Community Server.
```
So, I looking for the mysql error log and find the problem.
```text
2018-12-29T18:29:48.092739Z 0 [Warning] TIMESTAMP with implicit DEFAULT value is deprecated. Please use --explicit_defaults_for_timestamp server option (see documentation for more details).
2018-12-29T18:29:48.095908Z 0 [Note] /usr/sbin/mysqld (mysqld 5.7.24-0ubuntu0.18.10.1) starting as process 29186 ...
2018-12-29T18:29:48.102760Z 0 [Note] InnoDB: PUNCH HOLE support available
2018-12-29T18:29:48.102821Z 0 [Note] InnoDB: Mutexes and rw_locks use GCC atomic builtins
2018-12-29T18:29:48.102829Z 0 [Note] InnoDB: Uses event mutexes
2018-12-29T18:29:48.102836Z 0 [Note] InnoDB: GCC builtin __atomic_thread_fence() is used for memory barrier
2018-12-29T18:29:48.102843Z 0 [Note] InnoDB: Compressed tables use zlib 1.2.11
2018-12-29T18:29:48.102850Z 0 [Note] InnoDB: Using Linux native AIO
2018-12-29T18:29:48.103263Z 0 [Note] InnoDB: Number of pools: 1
2018-12-29T18:29:48.103433Z 0 [Note] InnoDB: Using CPU crc32 instructions
2018-12-29T18:29:48.108571Z 0 [Note] InnoDB: Initializing buffer pool, total size = 128M, instances = 1, chunk size = 128M
2018-12-29T18:29:48.108643Z 0 [ERROR] InnoDB: mmap(137428992 bytes) failed; errno 12
2018-12-29T18:29:48.108657Z 0 [ERROR] InnoDB: Cannot allocate memory for the buffer pool
2018-12-29T18:29:48.108665Z 0 [ERROR] InnoDB: Plugin initialization aborted with error Generic error
2018-12-29T18:29:48.108677Z 0 [ERROR] Plugin 'InnoDB' init function returned error.
2018-12-29T18:29:48.108684Z 0 [ERROR] Plugin 'InnoDB' registration as a STORAGE ENGINE failed.
2018-12-29T18:29:48.108692Z 0 [ERROR] Failed to initialize builtin plugins.
2018-12-29T18:29:48.108698Z 0 [ERROR] Aborting
```
So it looks like I do not have much ram for my innodb buffer pool. Using top I find
```text
top - 19:08:57 up 59 days, 27 min,  1 user,  load average: 0.00, 0.00, 0.00
Tasks:  85 total,   2 running,  82 sleeping,   1 stopped,   0 zombie
%Cpu(s):  0.3 us,  0.7 sy,  0.0 ni, 99.0 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st
MiB Mem :    481.2 total,     12.1 free,    384.2 used,     85.0 buff/cache
MiB Swap:      0.0 total,      0.0 free,      0.0 used.     84.6 avail Mem
```

So I looking for ```vi /etc/init.d/mysql``` and find the configuration file is at ```/etc/mysql/my.cnf```, change it to 
```
[mysqld]
innodb_buffer_pool_size = 16M
```
then start mysql server, every thing is fine now.
```
2018-12-29T18:49:54.139916Z 0 [Note] InnoDB: Initializing buffer pool, total size = 16M, instances = 1, chunk size = 16M
2018-12-29T18:49:54.146990Z 0 [Note] InnoDB: Completed initialization of buffer pool
2018-12-29T18:49:54.149357Z 0 [Note] InnoDB: If the mysqld execution user is authorized, page cleaner thread priority can be changed. See the man page of setpriority().
2018-12-29T18:49:54.191006Z 0 [Note] InnoDB: Highest supported file format is Barracuda.
2018-12-29T18:49:54.195145Z 0 [Note] InnoDB: Log scan progressed past the checkpoint lsn 2794888
2018-12-29T18:49:54.195187Z 0 [Note] InnoDB: Doing recovery: scanned up to log sequence number 2794897
2018-12-29T18:49:54.195199Z 0 [Note] InnoDB: Database was not shutdown normally!
2018-12-29T18:49:54.195208Z 0 [Note] InnoDB: Starting crash recovery.
2018-12-29T18:49:54.420013Z 0 [Note] InnoDB: Removed temporary tablespace data file: "ibtmp1"
2018-12-29T18:49:54.420070Z 0 [Note] InnoDB: Creating shared tablespace for temporary tables
2018-12-29T18:49:54.420157Z 0 [Note] InnoDB: Setting file './ibtmp1' size to 12 MB. Physically writing the file full; Please wait ...
```

But there is still a question: I start my mysql server yesterday successful, why it do not report this error that time?

References:
1. [Mysql配置文件/etc/my.cnf解析](https://www.jianshu.com/p/5f39c486561b)
2. [CentOS 7下MySQL服务启动失败的解决思路](https://www.cnblogs.com/ivictor/p/5146247.html)
3. [一次MySQL内存分配失败的解决](https://blog.csdn.net/foupwang/article/details/79431996)