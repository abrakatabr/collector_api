FROM rabbitmq:3.11.1-management-alpine

RUN apk add --no-cache --virtual build_deps curl
RUN curl -sL https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases/download/3.11.1/rabbitmq_delayed_message_exchange-3.11.1.ez > $RABBITMQ_HOME/plugins/rabbitmq_delayed_message_exchange.ez
RUN chown rabbitmq:rabbitmq $RABBITMQ_HOME/plugins/rabbitmq_delayed_message_exchange.ez
RUN rabbitmq-plugins enable rabbitmq_delayed_message_exchange
RUN rm -rf /var/cache/apk/*
RUN apk del build_deps

COPY enabled_plugins /etc/rabbitmq/enabled_plugins
