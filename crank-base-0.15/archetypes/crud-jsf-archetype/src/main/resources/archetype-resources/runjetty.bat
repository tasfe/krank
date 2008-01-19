rem call mvn -Dmaven.test.skip=true jetty:run-exploded 
rem call mvn -Dmaven.test.skip=true jetty:run-exploded 
call mvn -Dsun.lang.ClassLoader.allowArraySyntax=true -Dmaven.test.skip=true jetty:run 

