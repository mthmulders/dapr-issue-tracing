#!/usr/bin/env bash
set -euox pipefail

pushd service-a
export OTEL_SERVICE_NAME=service-a
export OTEL_TRACES_EXPORTER=zipkin
dapr run \
    --app-id service-a \
    --app-port 10001 \
    --dapr-http-port 3601 \
    --dapr-grpc-port 60001 \
    -- mvn spring-boot:run
popd