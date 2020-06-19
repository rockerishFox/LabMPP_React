package Utils_Networking;


import services.AbstractServer;
import utils.ConsoleColors;

import java.net.Socket;

public abstract class AbstractConcurrentServer extends AbstractServer {

    public AbstractConcurrentServer(int port) {
        super(port);
        System.out.println(ConsoleColors.YELLOW + "AbstractServer Concurrent initializat" + ConsoleColors.RESET);
    }

    protected void processRequest(Socket client) {
        Thread tw=createWorker(client);
        tw.start();
    }

    protected abstract Thread createWorker(Socket client) ;
}