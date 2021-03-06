package com.developer.workfair;

import java.io.IOException;

import com.developer.utils.ConnectionUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * @author: developer
 * @date: 2018/8/25 15:37
 * @description:workqueue队列演示 --接收消息 公平分发
 */

public class RecvWorkFair3 {

    private final static String QUEUE_NAME = "WORK_QUEUE";

    private static int count = 0;

    public static void main(String[] args) throws Exception {

        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.basicQos(1);//保证一次只发送一条消息

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                String msg = new String(body, "utf-8");

                System.out.println("3 recv:" + msg + " 接收时间：" + ConnectionUtils.getNowDate());

                count++;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    System.out.println("3 done");

                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
                System.out.println("总数:" + count);
            }
        };

        channel.basicConsume(QUEUE_NAME, false, consumer);

    }
}
