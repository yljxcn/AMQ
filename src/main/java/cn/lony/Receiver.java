package cn.lony;

import javax.jms.*;

public class Receiver {
    public static void main(String[] args) throws JMSException {
        //根据用户名，密码，url创建一个连接工厂
        ConnectionFactory factory = new org.apache.activemq.ActiveMQConnectionFactory(org.apache.activemq.ActiveMQConnectionFactory.DEFAULT_USER, org.apache.activemq.ActiveMQConnectionFactory.DEFAULT_PASSWORD, org.apache.activemq.ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
        //从工厂中获取一个连接
        Connection connection = factory.createConnection();
        connection.start();
        //创建一个session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //创建一个到达的目的地，其实想一下就知道了，activemq 不可能同时只能跑一个队列吧，这里就是连接了一个名为"text-msg"的队列，这个会话将会到这个队列，当然，如果这个队列不存在，将会被创建
        Destination destination = session.createQueue("text-msg");
        //根据session，创建一个接收者对象
        MessageConsumer consumer = session.createConsumer(destination);


        // 实现一个消息的监听器
        // 实现这个监听器后，以后只要有消息，就会通过这个监听器接收到
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    //获取到接收的数据
                    String text = ((TextMessage) message).getText();
                    System.out.println(text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        // 关闭接收端，也不会终止程序哦
        // consumer.close();

    }
}
