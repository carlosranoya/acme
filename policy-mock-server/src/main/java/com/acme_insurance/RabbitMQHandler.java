package com.acme_insurance;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;


public class RabbitMQHandler {

    private String EXCHANGE_NAME = "policy.policy-created.exchange";

    private String HOST = "rabbitmq";

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    ConnectionFactory factory;
    public RabbitMQHandler() {

        factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = null;
        Channel channel = null;
        
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare("policy.quote-received.queue", true, false, false, null);
        } catch (IOException | TimeoutException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        DefaultConsumer consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(
                String consumerTag,
                Envelope envelope, 
                AMQP.BasicProperties properties, 
                byte[] body) throws IOException {
        
                    String message = new String(body, "UTF-8");
                    System.out.println("-----------------------");
                    System.out.println(message);
                    System.out.println("-----------------------");

                    Long policyId = new Random(System.currentTimeMillis()).nextLong(999999999);
                    send(message + "," + policyId.toString(), 5);

            }
        };

        try {
            channel.basicConsume("policy.quote-received.queue", true, consumer);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
    public void send(String message, int delay) {
        executorService.schedule(() -> {
            Connection connection;
            try {
                connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(EXCHANGE_NAME, "fanout", true);
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
            }
            catch (IOException | TimeoutException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }, delay, TimeUnit.SECONDS);

    }

}
