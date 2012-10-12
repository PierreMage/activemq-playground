import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActiveMQPlayground {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ActiveMQPlaygroundConfiguration.class);
        ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        fillQueue(context, threadPool, 1000 * 10);
        System.exit(0);
    }

    private static void fillQueue(final ApplicationContext context, ExecutorService threadPool, int millis) {
        long aMinuteFromNow = System.currentTimeMillis() + millis;
        while (System.currentTimeMillis() < aMinuteFromNow) {
            threadPool.execute(new Runnable() {
                public void run() {
                    QueueSender queueSender = context.getBean(QueueSender.class);
                    queueSender.send("Hello World!");
                }
            });
        }
    }
}
