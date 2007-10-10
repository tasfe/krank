#!/bin/sh
# mvn -Dmaven.test.skip=true jetty:run-exploded
# mvn -Dmaven.test.skip=true jetty:run-exploded
mvn -Dsun.lang.ClassLoader.allowArraySyntax=true -Dmaven.test.skip=true jetty:run
