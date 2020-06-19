package RPCprotocol;

import DTO.Mapper;
import DTO.MeciDTOS;
import DTO.UtilizatorDTOS;
import DTO.VanzareDTOS;
import domain.Meci;
import domain.Utilizator;
import domain.Vanzare;
import services.IService;
import utils.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RPCProxy implements IService {
    private String host;
    private int port;
    private Observer client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    private boolean isInitialized = false;

    public RPCProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }


    private void initializeConnection() {
        if (!this.isInitialized) {

            try {
                connection = new Socket(host, port);
                output = new ObjectOutputStream(connection.getOutputStream());
                output.flush();
                input = new ObjectInputStream(connection.getInputStream());
                finished = false;
                startReader();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.isInitialized = true;
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private Response readResponse() {
        Response response = null;
        try {
            response = qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void sendRequest(Request request) {
        try {
            output.writeObject(request);
            output.flush();
        } catch (Exception e) {
            System.out.println("EXCEPTION THROWN: PROXY SEND REQUEST!");
            //throw new TeledonException("Error sending object "+e);
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.UPDATE;
    }

    private void handleUpdate(Response response) {
        if (response.type() == ResponseType.UPDATE) {
            //CharityCase charityCase = DTOUtils.getFromDTO((CharityCaseDTO)response.data());
            //System.out.println("Case "+charityCase.getName()+" is update");
            //Iterable<CharityCase> charityCases = (Iterable<CharityCase>)response.data();
            one_read = true;
            try {
                client.update();
            } catch (Exception e) {
                System.out.println("EXCEPTION THROWN: PROXY HANDLEUPDATE METHOD");
            }

        }
    }

    private boolean one_read = false;

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (isUpdate((Response) response)) {
                        handleUpdate((Response) response);
                    } else {

                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }

    public void addObserver(Observer e){
        this.client = e;
    }

    //  ISERVICE IMPLEMENTATION

    @Override
    public Meci findMeciById(int id) {
        initializeConnection();
        Request request = new Request.Builder().type(RequestType.FIND_MECI_BY_ID).data(id).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR)
            return null;

        return Mapper.fromMeciDTOS((MeciDTOS) response.data());
    }

    @Override
    public int getTotalBileteCumparateForMeci(Meci m) {
        initializeConnection();
        MeciDTOS meci = Mapper.toMeciDTOS(m);
        Request request = new Request.Builder().type(RequestType.GET_TOTAL_BILETE_CUMPARATE_FOR_MECI).data(meci).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR)
            return -1;

        return (int) response.data();
    }

    @Override
    public int getLastFreeIdVanzari() {
        initializeConnection();
        Request request = new Request.Builder().type(RequestType.GET_LAST_FREE_ID_VANZARI).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR)
            return -1;

        return (int) response.data();
    }

    @Override
    public void addVanzare(Vanzare c) {
        initializeConnection();
        VanzareDTOS v = Mapper.toVanzaareDTOS(c);
        Request request = new Request.Builder().type(RequestType.ADD_VANZARE).data(v).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR)
            System.out.println(response.data().toString());
    }

    @Override
    public void addUser(Utilizator u) {

    }

    @Override
    public Iterable<Utilizator> getAllUtilizatori() {
        return null;
    }

    @Override
    public Utilizator findByUsername(String username) {
        initializeConnection();
        Request request = new Request.Builder().type(RequestType.GET_BY_USERNAME).data(username).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR)
            return null;

        return Mapper.fromUtilizatorDTOS((UtilizatorDTOS) response.data());
    }

    @Override
    public Iterable<Meci> getAllMeciuri() {
        initializeConnection();
        Request request = new Request.Builder().type(RequestType.GET_ALL_MECIURI).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR)
            return null;

        Iterable<MeciDTOS> meciuri = (Iterable<MeciDTOS>) response.data();
        return Mapper.fromMeciDTOS(meciuri);
    }

    @Override
    public boolean addToLoggedInUsers(Utilizator u) {
        initializeConnection();
        Request request = new Request.Builder().type(RequestType.ADD_TO_LOGGED_USERS).data(Mapper.toUtilizatorDTOS(u)).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR)
            return false;
        return (boolean) response.data();
    }

    public boolean addToLoggedInUsers(Utilizator u, Observer client) {
        initializeConnection();
        Request request = new Request.Builder().type(RequestType.ADD_TO_LOGGED_USERS).data(Mapper.toUtilizatorDTOS(u)).build();
        sendRequest(request);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR)
            return false;

        boolean ans =  (boolean) response.data();
        if(ans)
            addObserver(client);
        return ans;
    }

    @Override
    public void logOut(Utilizator u) {

    }


}
