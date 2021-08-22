FROM tomcat:9.0

ENV PORT=8080

# set the port 
# RUN sed -i -e "s/<Connector port=\"8080\"/<Connector port=\"$PORT\"/g" /usr/local/tomcat/conf/server.xml

COPY ./target/*.war $CATALINA_HOME/webapps/

CMD ["catalina.sh", "run" , "-b", "0.0.0.0:$PORT"]
