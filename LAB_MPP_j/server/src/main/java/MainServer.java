import Utils_Networking.RPCConcurrentServer;
import domain.Utilizator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.AbstractServer;
import services.UtilizatorService;
import utils.ConsoleColors;

import java.io.Console;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class MainServer {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("ServiceMainApp.xml");
        UtilizatorService serviceUser = context.getBean(UtilizatorService.class);
        AbstractServer server = new RPCConcurrentServer(55555, serviceUser);

        try {
            server.start();
        } catch (Exception e){
            System.out.println(ConsoleColors.RED_BOLD + e.getMessage() + ConsoleColors.RESET );
        }
    }
}
