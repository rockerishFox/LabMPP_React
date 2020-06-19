package Utils_Networking;

import RPCprotocol.RPCWorker;
import services.IService;
import utils.ConsoleColors;

import java.net.Socket;

public class RPCConcurrentServer extends AbstractConcurrentServer {
    private IService service;

    public RPCConcurrentServer(int port, IService service) {
        super(port);
        this.service = service;
        System.out.println(ConsoleColors.YELLOW + "RPCConcurrentServer initializat" + ConsoleColors.RESET);
    }

    @Override
    protected Thread createWorker(Socket client) {
        RPCWorker worker = new RPCWorker(service, client);

        Thread tw = new Thread(worker);
        return tw;
    }
}
