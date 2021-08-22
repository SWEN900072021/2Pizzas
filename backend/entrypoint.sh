#! bash
sed -i -e "s/8080/$PORT/g" $CATALINA_HOME/conf/server.xml
$CATALINA_HOME/bin/catalina.sh run