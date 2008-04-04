#!/bin/sh

mvn -Dlog4j.configuration=file:./log4j.xml  jetty:run
