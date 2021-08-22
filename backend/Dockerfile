FROM tomcat:9.0

COPY ./target/*.war $CATALINA_HOME/webapps/
ADD ./entrypoint.sh ./entrypoint.sh
RUN chmod 777 ./entrypoint.sh

ENV PORT=8081

EXPOSE $PORT

CMD [ "bash", "./entrypoint.sh" ]
