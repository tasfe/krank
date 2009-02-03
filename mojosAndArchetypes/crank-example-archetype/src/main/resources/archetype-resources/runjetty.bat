set MAVEN_OPTS="-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9009 -ea"
mvn  -Dlog4j.configuration=file:./log4j.xml  jetty:run




