APP_NAME := "api-gateway"

# build application
build:
    mvn clean package

# build docker image
docker: build
    docker build -t {{APP_NAME}}:latest .
    docker tag {{APP_NAME}}:latest localhost:5005/{{APP_NAME}}:latest
    docker push localhost:5005/{{APP_NAME}}:latest
    docker images | grep "{{APP_NAME}}"

# run application
run: build
    mvn spring-boot:run -Dspring-boot.run.profiles=local
