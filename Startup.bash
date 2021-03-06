#run
cd /home/code/java/HTTP_Server
sudo nohup mvn exec:java -Dexec.mainClass="Controller.HTTP_Server" &

#debug
mvn exec:exec -Dexec.executable="java" -Dexec.args="-classpath %classpath -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:11111 Controller.HTTP_Server"


#test
cd D:\Softwares\Apache24\bin
./ab -n 500 -c 20 http://yifu.click/


1.CPU占用最多的前10个进程：
ps auxw|head -1;ps auxw|sort -rn -k3|head -10
2.内存消耗最多的前10个进程
ps auxw|head -1;ps auxw|sort -rn -k4|head -10
3.虚拟内存使用最多的前10个进程
ps auxw|head -1;ps auxw|sort -rn -k5|head -10

4.也可以试试

ps auxw --sort=rss
ps auxw --sort=%cpu

5.看看几个参数含义

%MEM 进程的内存占用率
MAJFL is the major page fault count,
VSZ 进程所使用的虚存的大小
RSS 进程使用的驻留集大小或者是实际内存的大小(RSS is the "resident set size" meaning physical memory used)
TTY 与进程关联的终端（tty）

    串行端口终端（/dev/ttySn）
    伪终端（/dev/pty/）
    控制终端（/dev/tty）
    控制台终端（/dev/ttyn,   /dev/console）
    虚拟终端(/dev/pts/n)


STAT 检查的状态：进程状态使用字符表示的，如R（running正在运行或准备运行）、S（sleeping睡眠）、I（idle空闲）、Z (僵死)、D（不可中断的睡眠，通常是I/O）、P（等待交换页）、W（换出,表示当前页面不在内存）、N（低优先级任务）T(terminate终止)、W has no resident pages


    D    不可中断     Uninterruptible sleep (usually IO)
    R    正在运行，或在队列中的进程
    S    处于休眠状态
    T    停止或被追踪
    Z    僵尸进程
    W    进入内存交换（从内核2.6开始无效）
    X    死掉的进程

    <    高优先级
    N    低优先级
    L    有些页被锁进内存
    s    包含子进程
    +    位于后台的进程组；
    l    多线程，克隆线程  multi-threaded (using CLONE_THREAD, like NPTL pthreads do)