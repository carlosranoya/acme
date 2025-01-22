package com.acme_insurance.quote.infra.broker;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.acme_insurance.quote.application.repository.CustomQuoteRepository;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

@Component
public class RabbitMQHandler {

    @Autowired
    private CustomQuoteRepository quoteRepository;

    private String EXCHANGE_NAME = "quote.quote-received.exchange";

    private String QUEUE_NAME = "quote.policy-created.queue";

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    ConnectionFactory factory;
    
        private boolean isConsuming = false;
    
        public RabbitMQHandler() {
    
            factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
    
        }
    
        private void connectAndListen() throws IOException, TimeoutException {
            
            System.out.println("Trying to connect to RabbitMq ...");
            Connection connection = null;
            Channel channel = null;
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            System.out.println("Connection OK.");
            DefaultConsumer consumer = new DefaultConsumer(channel) {
    
                @Override
                public void handleDelivery(
                    String consumerTag,
                    Envelope envelope, 
                    AMQP.BasicProperties properties, 
                    byte[] body) throws IOException {
            
                        String message = new String(body, "UTF-8");
                        System.out.println("--------------------");
                        System.out.println(message);
                        System.out.println("--------------------");
    
                        String[] ids = message.split(",");
    
                        Long quoteId = Long.parseLong(ids[0]);
                        Long policyId = Long.parseLong(ids[1]);
    
                        quoteRepository.updatePolicyByCustomId(quoteId, policyId);
    
                }
            };
            channel.basicConsume(QUEUE_NAME, true, consumer);
            isConsuming = true;
    }

    public void startConsuming() {
  
        try {
            connectAndListen();
        } catch (IOException | TimeoutException e) {
            executorService.schedule(() -> {
                try {
                    connectAndListen();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (TimeoutException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }, 4000, TimeUnit.MILLISECONDS);
        }

    }

    public void send(String message, int delay) {
        if (!isConsuming) {
            startConsuming();
        }
        executorService.schedule(() -> {
            Connection connection;
            try {
                connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.exchangeDeclare(EXCHANGE_NAME, "fanout", true);
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
                System.out.println("[x] Sent '" + message + "'");
            }
            catch (IOException | TimeoutException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }, delay, TimeUnit.SECONDS);

    }

}
