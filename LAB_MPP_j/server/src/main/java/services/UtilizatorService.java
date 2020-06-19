package services;

import domain.Meci;
import domain.Utilizator;
import domain.Vanzare;
import repository.MeciJdbcRepository;
import repository.UtilizatorJdbcRepository;
import repository.VanzareJdbcRepository;
import utils.Observer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UtilizatorService implements IService {
    private Map<Utilizator,Observer> loggedIn;

    private UtilizatorJdbcRepository repo;
    private MeciJdbcRepository repoMeci;
    private VanzareJdbcRepository repoVanzare;

    public UtilizatorService(UtilizatorJdbcRepository repo, MeciJdbcRepository meci, VanzareJdbcRepository vanzari) {
        this.repo = repo;
        this.repoMeci = meci;
        this.repoVanzare = vanzari;
        this.loggedIn = new ConcurrentHashMap<>();
    }

    public Meci findMeciById(int id) {
        return this.repoMeci.findOne(id);
    }

    public int getTotalBileteCumparateForMeci(Meci m) {
        return this.repoVanzare.getTotalBileteCumparateForMeci(m);
    }

    public int getLastFreeIdVanzari() {
        return this.repoVanzare.calculateLastFreeId();
    }

    public void addVanzare(Vanzare c) {
        this.repoVanzare.save(c);
        this.notifyObservers();
    }

    public void addUser(Utilizator u) {
        this.repo.save(u);
        //notifyObservers
    }

    public Iterable<Utilizator> getAllUtilizatori() {
        return repo.findAll();
    }

    public Utilizator findByUsername(String username) {
        for (Utilizator user : this.repo.findAll()) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    public Iterable<Meci> getAllMeciuri() {
        return this.repoMeci.findAll();
    }

    public boolean addToLoggedInUsers(Utilizator u) {
        for (Utilizator user : loggedIn.keySet()) {
            if (user.getUsername().equals(u.getUsername()))
                return false;
        }
        this.addObserver(u,null);
        return true;
    }

    @Override
    public boolean addToLoggedInUsers(Utilizator u, Observer obs) {
        for (Utilizator user : loggedIn.keySet()) {
            if (user.getUsername().equals(u.getUsername()))
                return false;
        }
        this.addObserver(u,obs);
        return true;
    }


    private void addObserver(Utilizator u, Observer o) {
        loggedIn.put(u,o);
    }
    public synchronized void notifyObservers(){
        loggedIn.values().forEach(x->x.update());
    }


    public void logOut(Utilizator u) {
        loggedIn.remove(u);
    }
}
