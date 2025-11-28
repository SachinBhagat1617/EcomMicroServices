#!/bin/bash

cd ../..

# Build all services
cd eureka && mvn clean compile jib:build && cd ..
cd gateway && mvn clean compile jib:build && cd ..
cd configServer && mvn clean compile jib:build && cd ..
cd order && mvn clean compile jib:build && cd ..
cd user && mvn clean compile jib:build && cd ..
cd product && mvn clean compile jib:build && cd ..
cd notification && mvn clean compile jib:build && cd ..