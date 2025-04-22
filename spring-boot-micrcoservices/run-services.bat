@echo off
echo Starting DB servers, RabbitMQ, Mailhog and keycloak...
docker compose -f spring-boot-microservices/deployment/docker-compose/infra.yaml up -d

:: Move to catalog-service directory
cd spring-boot-microservices/catalog-service/

echo Creating catalog-service JAR...
call mvnw clean package -DskipTests

echo Building catalog-service Docker image...
docker build -t catalog-service .

echo Starting catalog-service container...
docker run -d --network spring-boot-microservices_default -p 8081:8081 --name catalog-service catalog-service

:: Move back to the project root
cd ..\..

:: Move to order-service directory
cd spring-boot-microservices/order-service/

echo Creating order-service JAR...
call mvnw clean package -DskipTests

echo Building order-service Docker image...
docker build -t order-service .

echo Starting order-service container...
docker run -d --network spring-boot-microservices_default -p 8082:8082 --name order-service order-service

:: Move back to the project root
cd ..\..

:: Move to notification-service directory
cd spring-boot-microservices/notification-service/

echo Creating notification-service JAR...
call mvnw clean package -DskipTests

echo Building notification-service Docker image...
docker build -t notification-service .

echo Starting notification-service container...
docker run -d --network spring-boot-microservices_default -p 8083:8083 --name notification-service notification-service

:: Move back to the project root
cd ..\..

:: Move to api-gateway directory
cd spring-boot-microservices/api-gateway/

echo Creating api-gateway JAR...
call mvnw clean package -DskipTests

echo Building api-gateway Docker image...
docker build -t api-gateway .

echo Starting api-gateway container...
docker run -d --network spring-boot-microservices_default -p 8989:8989 --name api-gateway api-gateway

:::: Move back to the project root
cd ..\..

:::: Move to bookstore-webapp directory
cd spring-boot-microservices/bookstore-webapp/

::echo Creating bookstore-webapp JAR...
call mvnw clean package -DskipTests

::echo Building bookstore-webapp Docker image...
docker build -t bookstore-webapp .

::echo Starting bookstore-webapp container...
docker run -d --network spring-boot-microservices_default -p 8080:8080 --name bookstore-webapp bookstore-webapp

::echo All services are up and running!

:: Keep the window open to check for errors
pause
exit /b