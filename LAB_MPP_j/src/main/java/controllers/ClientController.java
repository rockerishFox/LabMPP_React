package controllers;

import domain.Meci;
import domain.MeciDTO;
import domain.Utilizator;
import domain.Vanzare;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import jdk.internal.jline.internal.Nullable;
import services.IService;
import utils.AppEvents;
import utils.IEvent;
import utils.Observable;
import utils.Observer;

public class ClientController implements IService, Observer<AppEvents>, Observable {
    IService service;

    private ObservableList<Meci> meciuri;
    private Observer observer;

    public ClientController(IService service) {
        this.service = service;
        meciuri = FXCollections.observableArrayList();
    }

    private void addAllMeciuri(){
        meciuri.clear();
        for( Meci m : service.getAllMeciuri())
            meciuri.add(m);
    }

    public Meci findMeciById(int id) {
        return service.findMeciById(id);
    }

    public int getTotalBileteCumparateForMeci(Meci m) {
        return service.getTotalBileteCumparateForMeci(m);
    }

    public int getLastFreeIdVanzari() {
        return service.getLastFreeIdVanzari();
    }

    public void addVanzare(Vanzare c) {
        service.addVanzare(c);
        refreshMeciuri();
    }

    public void addUser(Utilizator u) {
        service.addUser(u);
    }

    public Iterable<Utilizator> getAllUtilizatori() {
        return service.getAllUtilizatori();
    }

    public Utilizator findByUsername(String username) {
        return service.findByUsername(username);
    }

    public Iterable<Meci> getAllMeciuri() {
        addAllMeciuri();
        return meciuri;
    }

    public boolean addToLoggedInUsers(Utilizator u) {
        return service.addToLoggedInUsers(u, this);
    }

    @Override
    public boolean addToLoggedInUsers(Utilizator u, Observer obs) {
        return service.addToLoggedInUsers(u, obs);
    }

    public void logOut(Utilizator u) {
        service.logOut(u);
    }

    @Override
    public void update() {
        // daca nu ii dam runLater, producem un deadlock
        Platform.runLater(this::refreshMeciuri);
    }

    private void refreshMeciuri() {
        meciuri.clear();
        service.getAllMeciuri().forEach(x -> meciuri.add(x));
        notifyObservers(null);
    }

    @Override
    public void addObserver(Observer e) {
        this.observer = e;
    }

    @Override
    public void removeObserver(Observer e) {
        this.observer = null;
    }

    @Override
    public void notifyObservers(IEvent iEvent) {
        if (this.observer != null)
            this.observer.update();
    }
}
