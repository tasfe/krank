call mvn ^
-Ddb=mysql ^
-Dmaven.test.skip=true  ^
-Dlog4j.configuration=file:./log4j.xml ^
jetty:run ^
-o

