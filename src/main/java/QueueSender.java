import org.springframework.jms.core.JmsTemplate;

public class QueueSender {

    private final JmsTemplate jmsTemplate;

    private final String queueName;

    public QueueSender(final JmsTemplate jmsTemplate, String queueName) {
        this.jmsTemplate = jmsTemplate;
        this.queueName = queueName;
    }

    public void send(final String message) {
        jmsTemplate.convertAndSend(queueName, message);
    }
}
