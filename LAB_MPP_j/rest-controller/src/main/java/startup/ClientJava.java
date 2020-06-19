package startup;

import domain.Echipa;
import domain.Meci;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import repository.EchipaJdbcRepository;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ClientJava {
    private static RestClient restClient = new RestClient();

    public static void main(String[] args) {
        //Meci[] trips = restClient.getAll();

        for (Meci meci : restClient.getAll()) {
            System.out.println(meci);
        }

        ApplicationContext context = new ClassPathXmlApplicationContext("MainApp.xml");
        EchipaJdbcRepository repo = context.getBean(EchipaJdbcRepository.class);


        Echipa e1 = repo.findOne(1);
        Echipa e2 = repo.findOne(2);


        //Integer integer, Double pret, Integer nrBilete, String scor, Echipa[] teams)
        restClient.create(new Meci(21, 20d, 200, "20-20", e1, e2 ));

        for (Meci trip : restClient.getAll()) {
            System.out.println(trip);
        }
        restClient.update(new Meci(21, 20d, 1000, "20-20", e1, e2), 21);

        for (Meci trip : restClient.getAll()) {
            System.out.println(trip);
        }
        restClient.delete(21);
        for (Meci trip : restClient.getAll()) {
            System.out.println(trip);
        }
    }
}
