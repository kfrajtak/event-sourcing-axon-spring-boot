#!/usr/bin/env python
from datetime import datetime
from opentelemetry import trace
from opentelemetry.sdk.resources import SERVICE_NAME, Resource
from opentelemetry.exporter.otlp.proto.grpc.trace_exporter import (
    OTLPSpanExporter,
)
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchSpanProcessor

import pika
import os

host = os.getenv('RABBITMQHOST', 'localhost')
print("RabbitMQ host:", host)

span_exporter = OTLPSpanExporter(
    # optional
    # endpoint="myCollectorURL:4317",
    # credentials=ChannelCredentials(credentials),
    # headers=(("metadata", "metadata")),
)
tracer_provider = TracerProvider(resource=Resource.create({SERVICE_NAME: "python-service"}))
trace.set_tracer_provider(tracer_provider)
span_processor = BatchSpanProcessor(span_exporter)
tracer_provider.add_span_processor(span_processor)

# Configure the tracer to use the collector exporter
tracer = trace.get_tracer_provider().get_tracer(__name__)

def callback(ch, method, properties, body):
    with tracer.start_as_current_span("callback"):
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
