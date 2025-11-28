#!/bin/bash

cd ../..

#Build all services
cd eureka && mvn clean package -DskipTests && cd ..
cd gateway && mvn clean package -DskipTests && cd ..
cd configserver && mvn clean package -DskipTests && cd ..
cd order && mvn clean package -DskipTests && cd ..
cd user && mvn clean package -DskipTests && cd ..
cd product && mvn clean package -DskipTests && cd ..
cd notification && mvn clean package -DskipTests && cd ..