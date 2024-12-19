package com.majian.springbootinit.judge;

import com.majian.springbootinit.model.entity.rabbitMqConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class MyRabbitMq {
    /**
     * 用于创建测试程序用到的交换机和队列（只用在程序启动前执行一次）
     */
    public static void initRabbitmq() {
        ConnectionFactory factory = new ConnectionFactory();
        Connection connection = null;
        Channel channel = null;
        try {
            factory.setHost("192.168.242.136"); // 替换为虚拟机的实际IP地址
            factory.setPort(5672);
            factory.setUsername("myuser");
            factory.setPassword("mypass");
            connection = factory.newConnection();
            channel = connection.createChannel();
            String EXCHANGE_NAME = rabbitMqConstant.EXCHANGE_NAME;
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            // 创建队列，随机分配一个队列名称
            String queueName = rabbitMqConstant.QUEUE_NAME;
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, rabbitMqConstant.ROUTING_KEY);
        } catch (Exception e) {
            log.error("错误初始化");
        }finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    log.error("关闭通道失败", e);
                } catch (TimeoutException e) {
                    throw new RuntimeException(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    log.error("关闭连接失败", e);
                }
            }
        }
    }
}
