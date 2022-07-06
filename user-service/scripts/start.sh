#!/bin/bash

[ -z "$JAVA_XMS" ] && JAVA_XMS=512m
[ -z "$JAVA_XMX" ] && JAVA_XMX=512m

set -e

 # OpenTelemetry:
 # https://github.com/open-telemetry/opentelemetry-java-instrumentation
 JAVA_OPTS="${JAVA_OPTS} \
   -Xms${JAVA_XMS} \
   -Xmx${JAVA_XMX}"
#   -Dotel.service.name=user-service-java \
#   -Dotel.traces.exporter=jaeger \
#   -Dotel.exporter.jaeger.endpoint=http://app1.daan-vinken.sandbox01.adyen.com:14250 \
#   -javaagent:/app/bin/agents/opentelemetry-javaagent.jar"



exec java ${JAVA_OPTS} \
  -jar "target/user-service-0.0.1-SNAPSHOT.jar" \
