#!/bin/sh
MAVEN_OPTS="-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=9009 -ea"
export MAVEN_OPTS
echo $MAVEN_OPTS
mvn  codegen:codegen
