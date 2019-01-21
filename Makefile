server:
    sudo nohup mvn exec:java -Dexec.mainClass="Controller.HTTP_Server" &

local:
    mvn exec:java -Dexec.mainClass="Controller.HTTP_Server"

debug:
    mvn exec:exec -Dexec.executable="java" -Dexec.args="-classpath %classpath -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:11111 Controller.HTTP_Server"