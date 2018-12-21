cd /home/code/java/http/HTTP_Server
nohup java -cp ../lib/json.jar:../lib/jdbc.jar:./ HTTP_Server &

cd D:\Softwares\Apache24\bin
./ab -n 100 -c 10 http://yifu.click/