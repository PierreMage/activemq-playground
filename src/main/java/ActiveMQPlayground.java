import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ActiveMQPlayground {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ActiveMQPlaygroundConfiguration.class);
        QueueSender queueSender = context.getBean(QueueSender.class);
        queueSender.send("Hello World!");
        System.exit(0);
    }
}
