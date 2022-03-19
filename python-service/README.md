1. Open the `rabbitmq-demo` in Visual Studio Code
1. Stop the RabbitMQ container from the previous project (or any RabbitMQ container)
1. Open the project in container (Ctrl+Shift+P and choose "Remote-Containers: Reopen in container")
1. Log into RabbitMQ Management (RM) at http://localhost:15672/, use `guest` as username and password
1. Run the `./send.py` script from console in VS.Code
   - the script will create queue `hello`
   - message are send to default exchange
1. Switch to RM and see that the `hello` queue has 1 item queued, you can also view the message content
1. Run the script few more times - messages are queued and waiting for consumer(s)
