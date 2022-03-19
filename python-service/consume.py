#!/usr/bin/env python
from datetime import datetime
import pika
import os

host = os.getenv('RABBITMQHOST', 'localhost')
print("RabbitMQ host:", host)


def callback(ch, method, properties, body):
    print(" [x] Received %r" % body)


# https://www.rabbitmq.com/tutorials/tutorial-one-python.html
# establish a connection with RabbitMQ server.
credentials = pika.PlainCredentials('consumer', 'empty')
parameters = pika.ConnectionParameters(host,
                                       5672,
                                       '/',
                                       credentials)

connection = pika.BlockingConnection(parameters)
try:
    channel = connection.channel()

    # make sure the recipient queue exists
    # create a hello queue to which the message will be delivered:
    # channel.queue_declare(queue='hello')

    now = datetime.now()

    channel.basic_consume(queue='AccountActivated',
                          auto_ack=True,
                          on_message_callback=callback)

    print(' [*] Waiting for messages. To exit press CTRL+C')
    channel.start_consuming()
finally:
    connection.close()
