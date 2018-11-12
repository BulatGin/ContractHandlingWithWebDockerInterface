FROM rabbitmq:3.7-management-alpine

ADD rabbitmq.config /etc/rabbitmq/
ADD definitions.json /etc/rabbitmq/

ENV RABBITMQ_LOGS /var/log/rabbitmq/rabbit.log
