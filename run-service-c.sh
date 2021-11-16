#!/usr/bin/env bash
set -euox pipefail

pushd service-c
export OTEL_SERVICE_NAME=service-c
export OTEL_TRACES_EXPORTER=zipkin
dapr run \
    --app-id service-c \
    --app-port 10003 \
    --dapr-http-port 3603 \
    --dapr-grpc-port 60003 \
    -- mvn spring-boot:run
popd