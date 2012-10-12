import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.AbstractMessageListenerContainer;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.jms.ConnectionFactory;
import javax.jms.ExceptionListener;
import javax.jms.MessageListener;

/**
 * <p>
 *   <ul>
 *     <li>Use {@link org.springframework.jms.connection.CachingConnectionFactory}</li>
 *     <li>All calls on the JmsTemplate are synchronous which means the calling thread will block until the method returns.</li>
 *     <li>So use {@link org.springframework.jms.listener.DefaultMessageListenerContainer}</li>
 *   </ul>
 * </p>
 * <p>
 *   <a href="http://codedependents.com/2009/10/16/efficient-lightweight-jms-with-spring-and-activemq/">Efficient Lightweight JMS with Spring and ActiveMQ</a>
 *   <a href="http://activemq.apache.org/spring-support.html">ActiveMQ Spring Support</a>
 * </p>
 */
@Configuration
@ImportResource("classpath:/properties-config.xml")
public class ActiveMQPlaygroundConfiguration {

    @Value("${jms.userName}")
    private String userName;

    @Value("${jms.password}")
    private String password;

    @Value("${jms.brokerUrl}")
    private String brokerUrl;

    @Value("${jms.sessionCacheSize}")
    private int sessionCacheSize;

    @Value("${jms.queueName}")
    private String queueName;

    @Value("${jms.listenerContainer.concurrency}")
    private String listenerContainerConcurrency;

    @Bean
    public QueueSender queueSender() {
        return new QueueSender(jmsTemplate(), queueName);
    }

    @Bean
    public AbstractMessageListenerContainer listenerContainer() {
        final DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
        defaultMessageListenerContainer.setConnectionFactory(connectionFactory());
        defaultMessageListenerContainer.setConcurrency(listenerContainerConcurrency);
        defaultMessageListenerContainer.setDestinationName(queueName);
        defaultMessageListenerContainer.setMessageListener(queueListener());
        return defaultMessageListenerContainer;
    }

    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(connectionFactory());
    }

    public ConnectionFactory connectionFactory() {
        final CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(amqConnectionFactory());
        cachingConnectionFactory.setExceptionListener(jmsExceptionListener());
        cachingConnectionFactory.setSessionCacheSize(sessionCacheSize);
        return cachingConnectionFactory;
    }

    public ConnectionFactory amqConnectionFactory() {
        return new ActiveMQConnectionFactory(userName, password, brokerUrl);
    }

    public ExceptionListener jmsExceptionListener() {
        return new JmsExceptionListener();
    }

    public MessageListener queueListener() {
        return new QueueListener();
    }

}
