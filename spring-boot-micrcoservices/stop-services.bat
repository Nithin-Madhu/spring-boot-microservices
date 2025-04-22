
::@echo off
::echo stopping all services
::cd spring-boot-microservices/
::docker stop catalog-service order-service bookstore-rabbitmq order-db catalog-db
::
::echo removing containers
::docker rm catalog-service order-service bookstore-rabbitmq order-db catalog-db
::
::echo removing images
::docker rmi catalog-service order-service
::
::echo All services are stopped & containers / images are removed!
::exit /b

@echo off
:: Disable command echoing for a cleaner output

echo Checking if services are running...

:: Define the list of services to check and stop
set SERVICES=catalog-service order-service bookstore-rabbitmq order-db catalog-db notifications-db mailhog notification-service api-gateway bookstore-webapp keycloak
set IMAGES=catalog-service order-service notification-service api-gateway bookstore-webapp

:: Loop through each service and stop it if it's running
for %%S in (%SERVICES%) do (
    docker ps -q --filter "name=%%S" >nul
    if not errorlevel 1 (
        echo Stopping %%S...
        docker stop %%S
    ) else (
        echo %%S is not running.
    )
)

:: Loop through each service and remove the container if it exists
for %%S in (%SERVICES%) do (
    docker ps -a -q --filter "name=%%S" >nul
    if not errorlevel 1 (
        echo Removing container %%S...
        docker rm %%S
    ) else (
        echo No container found for %%S.
    )
)

:: Loop through each service image and remove it if it exists
for %%I in (%IMAGES%) do (
    docker images -q %%I >nul
    if not errorlevel 1 (
        echo Removing image %%I...
        docker rmi %%I
    ) else (
        echo No image found for %%I.
    )
)

:: Display completion message
echo All necessary services are stopped, and containers/images are removed!

:: Pause to keep the command window open and allow the user to review messages
pause
exit /b
