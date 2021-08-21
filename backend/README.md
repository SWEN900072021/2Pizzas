# 2 Pizza Peperoni Planes backend

this is the backend for the Peperoni Planes application

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

### Running application

Requires installation of Apache Tomcat
Put build .war in webapps directory of Apache Tomcat installation (Tomcat will automatically pick up the .war and begin serving the application) 
If deploying locally open browser and navigate to http://localhost:8080/pepperoni-planes-1.0-SNAPSHOT/
