# Demo I
Publishing events from Spring Boot application to RabbitMQ

1. Start the Axon server (the event store) using `docker-compose` (stop services from last lab first)
1. Run the application `mvn spring-boot:run`
   - the application will create `spring-boot-exchange` exchange and `AccountActivated` queue 
   - queue will be bound to exchange with binding with routing key `AccountActivatedEvent`
   - that means `AccountActivatedEvent` events will be routed to `AccountActivated` queue since they are published to RabbitMQ with routing key (see `QueuePublisher` class)  
1. Open the Swagger UI at http://localhost:8080/swagger-ui.html
1. Open the Axon server dashboard http://localhost:8024/#query
1. Switch to RabbitMQ and open the `AccountActivatedEvent` queue
   - http://localhost:15672/#/queues/%2F/AccountActivatedEvent
   - queue has 1 item queued
   - you can also view the message content
   
## Demo II
Publishing events from Python application to RabbitMQ. Spring Boot application will act as a consumer and events will be sent from Python application.

1. Keep the application running
1. Open the `rabbitmq-demo` project in Visual Studio Code
1. Before reopening the project in container, inspect the docker bridge [network](http://localhost:9900/#/networks/bridge) and find the IP of the RabbitMQ container
1. Run the `./create-account.py` script from console in VS.Code
   - the script will 
   - message is send to the exchange and queue
1. RabbitMQ listener in Spring Boot application will handle it

## Demo III - Gateway

```
curl -X POST "http://localhost:8180/bank-service/bank-accounts" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"currency\": \"CZK\", \"overdraftLimit\": 0, \"startingBalance\": 2000}"
```

## Telemetry

1. Download the agent from https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.12.0/opentelemetry-javaagent.jar
2. Package `bank-service` and `gateway-service`
3. Open console for each service 
4. Set ENV variable with the name of the service 

**Java 1.8 must be used!**

### Gateway service
On Windows
```
set OTEL_SERVICE_NAME=gateway-service
java -javaagent:path\to\opentelemetry-javaagent.jar -Dotel.service.name=gateway-service -jar gateway-service\target\gateway-service-0.0.1-SNAPSHOT.jar       
```

On Unix (or in Windows bash)
```
export OTEL_SERVICE_NAME gateway-service
java -javaagent:/path/to/opentelemetry-javaagent.jar \
     -Dotel.service.name=gateway-service \     
     -jar ./gateway-service/target/gateway-service-0.0.1-SNAPSHOT.jar
```

### Bank Service
On Windows
```
set OTEL_SERVICE_NAME=bank-service
java -javaagent:path\to\opentelemetry-javaagent.jar -Dotel.resource.attributes=service.name=bank-service -jar bank-service\target\event-sourcing-axon-spring-boot-0.0.1-SNAPSHOT.jar       
```

On Unix (or in Windows bash)
```
export OTEL_SERVICE_NAME bank-service
java -javaagent:/path/to/opentelemetry-javaagent.jar \
     -Dotel.service.name=bank-service \
     -jar ./bank-service/target/event-sourcing-axon-spring-boot-0.0.1-SNAPSHOT.jar
```

