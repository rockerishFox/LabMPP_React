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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class RPCWorker implements Runnable, Observer {
    private IService server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public RPCWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;

        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        Response resp = new Response.Builder().type(ResponseType.UPDATE).build();
        System.out.println("Update client ...");
        try {
            sendResponse(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean one_read = false;

    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }

    private Response handleRequest(Request request) {
        Response response = null;
        String handlerName = "handle" + (request).type();
        System.out.println("Received request: " + request.type());
        try {
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response) method.invoke(this, request);
            System.out.println("Method " + handlerName + " invoked");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("Sending response " + response);
        output.writeObject(response);
        output.flush();
    }

    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();


    //  HANDLERS

    private Response handleGET_ALL_MECIURI(Request request) {
        System.out.println("Handling request " + request.type());
        try {
            Iterable<Meci> meciuri = this.server.getAllMeciuri();
            //Iterable<CharityCaseDTO> charityCaseDTOS = DTOUtils.getDTO(charityCases);
            one_read = true;
            return new Response.Builder().type(ResponseType.GET_ALL_MECIURI).data(Mapper.toMeciDTOS(meciuri)).build();
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_BY_USERNAME(Request request) {
        System.out.println("Handling request " + request.type());
        try {
            String username = (String) request.data();
            Utilizator user = this.server.findByUsername(username);
            one_read = true;
            return new Response.Builder().type(ResponseType.GET_BY_USERNAME).data(Mapper.toUtilizatorDTOS(user)).build();
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleADD_TO_LOGGED_USERS(Request request) {
        System.out.println("Handling request " + request.type());
        try {
            Utilizator user = Mapper.fromUtilizatorDTOS((UtilizatorDTOS) request.data());
            boolean succes = this.server.addToLoggedInUsers(user, this);
            one_read = true;
            return new Response.Builder().type(ResponseType.ADD_TO_LOGGED_USERS).data(succes).build();
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleFIND_MECI_BY_ID(Request request) {
        System.out.println("Handling request " + request.type());
        try {
            int id = (int) request.data();
            Meci meci = this.server.findMeciById(id);
            one_read = true;
            return new Response.Builder().type(ResponseType.FIND_MECI_BY_ID).data(Mapper.toMeciDTOS(meci)).build();
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_LAST_FREE_ID_VANZARI(Request request) {
        System.out.println("Handling request " + request.type());
        try {
            int id = this.server.getLastFreeIdVanzari();
            one_read = true;
            return new Response.Builder().type(ResponseType.GET_LAST_FREE_ID_VANZARI).data(id).build();
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_TOTAL_BILETE_CUMPARATE_FOR_MECI(Request request) {
        System.out.println("Handling request " + request.type());
        try {
            Meci meci = Mapper.fromMeciDTOS((MeciDTOS) request.data());
            int bilete = this.server.getTotalBileteCumparateForMeci(meci);
            one_read = true;
            return new Response.Builder().type(ResponseType.GET_TOTAL_BILETE_CUMPARATE_FOR_MECI).data(bilete).build();
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
    private Response handleADD_VANZARE(Request request) {
        System.out.println("Handling request " + request.type());
        try {
            Vanzare v = Mapper.fromVanzareDTOS((VanzareDTOS) request.data());
            this.server.addVanzare(v);
            one_read = true;
            return new Response.Builder().type(ResponseType.ADD_VANZARE).data(true).build();
        } catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    /*
    private Response handleLOGIN(Request request){
        System.out.println("Login request ..."+request.type());
        //VolunteerDTO udto=(VolunteerDTO)request.data();
        Volunteer user= (Volunteer)request.data();
        try {
            server.login(user.getId(),user.getPassword(), this);
            return okResponse;
        } catch (TeledonException e) {
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleGET_ALL_DONORS(Request request){
        System.out.println("Get all donors with a part of name ..."+request.type());

        String partOfName = (String)request.data();
        try{
            Iterable<Donor> donors = this.server.finDonorsByAPartOfName(partOfName);
            //Iterable<CharityCaseDTO> charityCaseDTOS = DTOUtils.getDTO(charityCases);
            one_read = true;
            return new Response.Builder().type(ResponseType.GET_DONORS).data(donors).build();
        }catch (TeledonException e){
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();

        }

    }

    private Response handleGET_ALL_CASES(Request request){
        System.out.println("Get all cases ..."+request.type());
        //VolunteerDTO udto=(VolunteerDTO)request.data();
        //Volunteer user= (Volunteer)request.data();
        try{
            Iterable<CharityCase> charityCases = this.server.findAllCharityCases();
            //Iterable<CharityCaseDTO> charityCaseDTOS = DTOUtils.getDTO(charityCases);
            one_read = true;
            return new Response.Builder().type(ResponseType.GET_CASES).data(charityCases).build();
        }catch (TeledonException e){
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }

    private Response handleADD_DONATION(Request request){
        System.out.println("Add Donation  ..."+request.type());
        //VolunteerDTO udto=(VolunteerDTO)request.data();
        //Volunteer user= (Volunteer)request.data();
        try{
            Object[] data_param = (Object[])request.data();
            this.server.saveDonation((Float)data_param[0],(String)data_param[1],(String)data_param[2],(String)data_param[3],(Integer)data_param[4],(String)data_param[5]);
            //Iterable<CharityCaseDTO> charityCaseDTOS = DTOUtils.getDTO(charityCases);
            one_read = true;
            return okResponse;
        }catch (TeledonException e){
            connected=false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }
    }
*/

}
