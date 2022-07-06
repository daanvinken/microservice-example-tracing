#!/bin/bash

JAVA_XMS=2048m
JAVA_XMX=2048m
set -e

 # OpenTelemetry:
 # https://github.com/open-telemetry/opentelemetry-java-instrumentation
 JAVA_OPTS="${JAVA_OPTS} \
   -Xms${JAVA_XMS} \
   -Xmx${JAVA_XMX}"
   #-Dotel.service.name=user-service-java \
   #-Dotel.traces.exporter=jaeger \
   #-Dotel.exporter.jaeger.endpoint=http://:14250 \
   #-javaagent:agents/opentelemetry-javaagent.jar"



exec java ${JAVA_OPTS} \
  -jar "target/payment-service-0.0.1-SNAPSHOT.jar" \
