#run
cd /home/code/java/HTTP_Server
sudo nohup mvn exec:java -Dexec.mainClass="Controller.HTTP_Server" &

#debug
mvn exec:exec -Dexec.executable="java" -Dexec.args="-classpath %classpath -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:11111 Controller.HTTP_Server"


#test
cd D:\Softwares\Apache24\bin
./ab -n 100 -c 10 http://yifu.click/