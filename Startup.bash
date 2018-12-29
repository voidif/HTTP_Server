cd /home/code/java/HTTP_Server
sudo nohup mvn exec:java -Dexec.mainClass="Controller.HTTP_Server" &

cd D:\Softwares\Apache24\bin
./ab -n 100 -c 10 http://yifu.click/