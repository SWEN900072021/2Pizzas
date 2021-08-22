# we are extending everything from tomcat:8.0 image ...
FROM tomcat:9.0

ENV PORT=8080

RUN sed -i -e 's/<Connector port="8080"/<Connector port="${PORT}"/g' /usr/local/tomcat/conf/server.xml

COPY ./target/*.war /usr/local/tomcat/webapps/