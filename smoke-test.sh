#!/bin/bash
set -x

NODE_PORT=$(kubectl get svc calculator-service -o=jsonpath='{.spec.ports[0].nodePort}')
./gradlew acceptanceTest -Dcalculator.url=http://192.168.59.102:${NODE_PORT}