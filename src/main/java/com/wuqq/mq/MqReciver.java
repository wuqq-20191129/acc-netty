package com.wuqq.mq;

import org.apache.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * @Classname MqReciver
 * @Description TODO
 * @Date 2021/1/27 10:52
 * @Created by mh
 */
public class MqReciver {

    private static Logger logger = Logger.getLogger(MqReciver.class);

    private DefaultMQPushConsumer consumer;

    private Thread thread;

    public MqReciver(){
        consumer = new DefaultMQPushConsumer("acc_consumer");
    }

    public void start() throws MQClientException {

        thread = new Thread(()->{
            try {
                logger.info("Starting consumer.....");
                consumer.setNamesrvAddr("172.20.18.34:9876");
                consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

                consumer.subscribe("acc_topic", "*");

                consumer.registerMessageListener(new MessageListenerConcurrently() {

                    @Override
                    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                                    ConsumeConcurrentlyContext context) {
                        System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);


                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                });


                consumer.start();
                //System.out.printf("Consumer Started.%n");
            } catch (MQClientException e) {
                logger.error("Create Rabbit MQ listener error: " + e.getMessage());
            }
        });

        thread.setDaemon(true);
        thread.start();
        //consumer.setNamesrvAddr("172.20.18.34:9876");
        //consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //
        //consumer.subscribe("acc_topic", "*");
        //
        //consumer.registerMessageListener(new MessageListenerConcurrently() {
        //
        //    @Override
        //    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
        //                                                    ConsumeConcurrentlyContext context) {
        //        System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
        //        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        //    }
        //});
        //
        //
        //consumer.start();

    }
}
