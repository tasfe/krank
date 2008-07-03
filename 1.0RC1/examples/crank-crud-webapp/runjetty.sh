#!/bin/sh

mvn -Ddb=mysql -Dlog4j.configuration=file:./log4j.xml  jetty:run
