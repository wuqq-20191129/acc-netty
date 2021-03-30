package com.wuqq.mq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Component;

/**
 * @Classname RocketMqSender
 * @Description TODO
 * @Date 2021/1/27 10:25
 * @Created by mh
 */
@Component
public class RocketMqSender  {

    //private String groupName;
    //
    //private String addr;
    private DefaultMQProducer producer;

    public RocketMqSender(){
        producer = new DefaultMQProducer("acc_group");
        producer.setNamesrvAddr("172.20.18.34:9876");
    }

    public void send(byte[] data) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        producer.start();

        Message message =new Message("acc_topic",data);
        SendResult sendResult = producer.send(message);

        System.out.printf("%s%n", sendResult);

        //producer.shutdown();
    }
}
