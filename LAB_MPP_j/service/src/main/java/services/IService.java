package services;

import domain.Meci;
import domain.Utilizator;
import domain.Vanzare;
import utils.Observer;

public interface IService {

    Meci findMeciById(int id);

    int getTotalBileteCumparateForMeci(Meci m);

    int getLastFreeIdVanzari();

    void addVanzare(Vanzare c);

    void addUser(Utilizator u);

    Iterable<Utilizator> getAllUtilizatori();

    Utilizator findByUsername(String username);

    Iterable<Meci> getAllMeciuri();

    boolean addToLoggedInUsers(Utilizator u);
    boolean addToLoggedInUsers(Utilizator u, Observer obs);

    void logOut(Utilizator u);

}
