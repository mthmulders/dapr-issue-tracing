#!/usr/bin/env bash
set -euox pipefail

pushd service-a
dapr run \
    --app-id service-a \
    --app-port 10001 \
    --dapr-http-port 3601 \
    --dapr-grpc-port 60001 \
    -- mvn spring-boot:run
popd