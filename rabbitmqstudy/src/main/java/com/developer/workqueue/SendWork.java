package com.developer.workqueue;

import com.developer.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @author: developer
 * @date: 2018/8/25 15:19
 * @description:workqueue队列演示
 */

public class SendWork {

    private final static String QUEUE_NAME = "WORK_QUEUE";

    public static void main(String[] args) throws Exception {

        Connection connection = ConnectionUtils.getConnection();

        //创建通道
        Channel channel = connection.createChannel();

        //创建队列声明
        //当已经定义好队列时，再将durable修改成true是不行的，rabbitmq不允许重新定义已经
        //存在的队列
        //解决方式：从控制台将队列删掉，或者重新定义一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);


        for (int i = 0; i < 50; i++) {

            String msg = "send hello" + i;
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());

            System.out.println("send msg:" + msg);
        }

        channel.close();

        connection.close();

    }
}
