# 2Pizzas Pepperoni Planes Backend

this is the backend for the Pepperoni Planes application

## Getting Started

### Testing

#### Execution

Test execution with maven  
```shell
mvn test
```

#### Test Report With Coverage

Test are run with coverage (coverage rules will also break build)
Find coverage report at ./target/site/jacoco/index.html

### Build

Build with maven
```shell
mvn package
```
Find executable at ./target/pepperoni-planes-1.0-SNAPSHOT.war

### Running Application

Requires installation of Apache Tomcat
Put build .war in webapps directory of Apache Tomcat installation (Tomcat will automatically pick up the .war and begin serving the application) 
If deploying locally open browser and navigate to http://localhost:8080/pepperoni-planes-1.0-SNAPSHOT/

### Running Application with Docker

Build the docker image
```shell
docker build . -f ./Dockerfile -t 2-pizzas-backend
```

Run the container
```shell
docker run -p 8080:8081 -t 2-pizzas-backend
```

Open browser and navigate to http://localhost:8080/pepperoni-planes-1.0-SNAPSHOT/
