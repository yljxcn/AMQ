package cn.lony;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Sender {
    public static void main(String[] args) throws JMSException {
        // 根据用户名，密码，url 创建一个连接工厂
        ConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER, ActiveMQConnectionFactory.DEFAULT_PASSWORD, ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
        //从工厂中获取一个连接
        Connection connection = factory.createConnection();
        connection.start();
        // 创建一个 session
        // 第一个参数:是否支持事务，如果为 true，则会忽略第二个参数，被 JMS 服务器设置为 SESSION_TRANSACTED
        // 第二个参数为 false 时，paramB 的值可为 Session.AUTO_ACKNOWLEDGE，Session.CLIENT_ACKNOWLEDGE，DUPS_OK_ACKNOWLEDGE 其中一个。
        // Session.AUTO_ACKNOWLEDGE 为自动确认，客户端发送和接收消息不需要做额外的工作。哪怕是接收端发生异常，也会被当作正常发送成功。
        // Session.CLIENT_ACKNOWLEDGE 为客户端确认。客户端接收到消息后，必须调用 javax.jms.Message 的 acknowledge 方法。JMS 服务器才会当作发送成功，并删除消息。
        // DUPS_OK_ACKNOWLEDGE 允许副本的确认模式。一旦接收方应用程序的方法调用从处理消息处返回，会话对象就会确认消息的接收；而且允许重复确认。
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 创建一个到达的目的地，其实想一下就知道了，activemq 不可能同时只能跑一个队列吧，这里就是连接了一个名为"text-msg"的队列，这个会话将会到这个队列，当然，如果这个队列不存在，将会被创建
        Destination destination = session.createQueue("text-msg");
        //从session中，获取一个消息生产者
        MessageProducer producer = session.createProducer(destination);
        //设置生产者的模式，有两种可选
        //DeliveryMode.PERSISTENT 当activemq关闭的时候，队列数据将会被保存
        //DeliveryMode.NON_PERSISTENT 当activemq关闭的时候，队列里面的数据将会被清空
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        //创建一条消息，当然，消息的类型有很多，如文字，字节，对象等,可以通过session.create..方法来创建出来
        TextMessage textMsg = session.createTextMessage("呵呵");
        for(int i = 0 ; i < 100 ; i ++){
            //发送一条消息
            producer.send(textMsg);
        }

        System.out.println("发送消息成功");
        //即便生产者的对象关闭了，程序还在运行哦
        producer.close();
    }
}
