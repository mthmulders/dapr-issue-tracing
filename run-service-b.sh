#!/usr/bin/env bash
set -euox pipefail

pushd service-b
export DAPR_API_PROTOCOL=grpc
export DAPR_API_METHOD_INVOCATION_PROTOCOL=grpc
export OTEL_SERVICE_NAME=service-b
export OTEL_TRACES_EXPORTER=zipkin
dapr run \
    --app-id service-b \
    --app-port 10002 \
    --dapr-http-port 3602 \
    --dapr-grpc-port 60002 \
    -- mvn spring-boot:run
popd