#!/usr/bin/env bash
set -euox pipefail

pushd service-b
dapr run \
    --app-id service-b \
    --app-port 10002 \
    --dapr-http-port 3602 \
    --dapr-grpc-port 60002 \
    -- mvn spring-boot:run
popd