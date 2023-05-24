FROM confluentinc/cp-kafka-connect:7.3.1

LABEL maintainer="ahmedmq"

USER root
RUN yum install unzip -y
RUN mkdir -p /usr/share/java/

WORKDIR /

RUN wget https://repo1.maven.org/maven2/io/debezium/debezium-connector-mysql/2.2.1.Final/debezium-connector-mysql-2.2.1.Final-plugin.tar.gz -O /tmp/debezium-connector-mysql.tar.gz \
&& tar -xvzf /tmp/debezium-connector-mysql.tar.gz --directory /usr/share/java/ \
&& rm /tmp/debezium-connector-mysql.tar.gz

RUN wget https://repo1.maven.org/maven2/io/debezium/debezium-connector-mongodb/2.2.1.Final/debezium-connector-mongodb-2.2.1.Final-plugin.tar.gz -O /tmp/debezium-connector-mongodb.tar.gz \
&& tar -xvzf /tmp/debezium-connector-mongodb.tar.gz --directory /usr/share/java/ \
&& rm /tmp/debezium-connector-mongodb.tar.gz