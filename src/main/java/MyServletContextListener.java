import model.RequestsLatencies;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.LinkedBlockingQueue;

public class MyServletContextListener implements ServletContextListener {

    public static final LinkedBlockingQueue<RequestsLatencies> stats = new LinkedBlockingQueue<>();

    private StatsDaemon daemonThread = null;

    public void contextInitialized(ServletContextEvent sce) {
        if ((daemonThread == null) || (!daemonThread.isAlive())) {
            daemonThread = new StatsDaemon();
            daemonThread.start();
        }
    }

    public void contextDestroyed(ServletContextEvent sce){
        daemonThread.interrupt();
    }

}
