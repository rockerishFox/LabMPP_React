package controllers;

import domain.Meci;
import domain.MeciDTO;
import domain.Utilizator;
import domain.Vanzare;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.IService;
import utils.Observable;
import utils.Observer;
import utils.Utils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public class ControllerUser implements Observer {
    IService serviceUser;
    Observable observable;
    private Stage primaryStage;
    Utilizator currentUser;
    ObservableList<MeciDTO> model = FXCollections.observableArrayList();


    @FXML
    Label labelWelcome;

    @FXML
    TableView<MeciDTO> tableViewMeciuri;

    @FXML
    TableColumn tableViewColumnId;
    @FXML
    TableColumn tableViewColumnPret;
    @FXML
    TableColumn tableViewColumnNrBilete;
    @FXML
    TableColumn tableViewColumnScor;
    @FXML
    TableColumn tableViewColumnDescriere;

    @FXML
    Button buttonMeciInfo;
    @FXML
    Button buttonRefresh;
    @FXML
    Button buttonFilterSoldOut;

    @FXML
    VBox vBoxClientInformation;

    @FXML
    TextField textFieldNrBilete;
    @FXML
    TextField textFieldNumeClient;

    public void startUp(IService serviceUser, Utilizator user, Stage primaryStage) {
        this.serviceUser = serviceUser;
        this.primaryStage = primaryStage;
        this.currentUser = user;

        labelWelcome.setText("Welcome, " + currentUser.getUsername());
        initializeMeciuri();
    }

    public void setObserver(Observable ob){
        this.observable = ob;
        this.observable.addObserver(this);
    }

    private void initializeMeciuri() {
        model.setAll(getMeciuri());
    }

    @FXML
    public void initialize() {
        tableViewColumnId.setCellValueFactory(new PropertyValueFactory<Meci, String>("id"));
        tableViewColumnDescriere.setCellValueFactory(new PropertyValueFactory<Meci, String>("descriere"));
        tableViewColumnPret.setCellValueFactory(new PropertyValueFactory<Meci, String>("pret"));
        tableViewColumnNrBilete.setCellValueFactory(new PropertyValueFactory<Meci, String>("nrBileteRamase"));
        tableViewColumnScor.setCellValueFactory(new PropertyValueFactory<Meci, String>("scor"));
        tableViewMeciuri.setItems(model);
        buttonMeciInfo.setDisable(true);
        vBoxClientInformation.setVisible(false);
        textFieldNumeClient.setText("");
        textFieldNrBilete.setText("");
    }

    // UI METHODS
    @FXML
    private void tableSelectionChanged() {
        buttonMeciInfo.setDisable(true);
        vBoxClientInformation.setVisible(false);
        MeciDTO meci = tableViewMeciuri.getSelectionModel().getSelectedItem();
        if (meci != null) {
            buttonMeciInfo.setDisable(false);
            if (!meci.getNrBileteRamase().equals("SOLD OUT!"))
                vBoxClientInformation.setVisible(true);
        }
    }

    @FXML
    public void buttonRefreshClicked() {
        initializeMeciuri();
        buttonMeciInfo.setDisable(true);
        vBoxClientInformation.setVisible(false);
    }

    @FXML
    public void buttonMeciInfoClicked() {
        MeciDTO meci = tableViewMeciuri.getSelectionModel().getSelectedItem();
        if (meci != null) {
            String aux = getMeciDTOInfo(meci);
            Utils.showInformation(aux);
        } else Utils.showWarning("No match has been selected!");
    }

    @FXML
    public void clearClientFields(){
        textFieldNumeClient.setText("");
        textFieldNrBilete.setText("");
    }

    @FXML
    public void buttonVanzareNouaClicked(){
        try{
            String client = textFieldNumeClient.getText();
            if (client.isEmpty()){
                Utils.showWarning("Nume de client null!");
                return;
            }
            int nr = Integer.parseInt(textFieldNrBilete.getText());

            MeciDTO selected = tableViewMeciuri.getSelectionModel().getSelectedItem();

            if(selected.getNrBileteRamase().equals("SOLD OUT!")){
                Utils.showWarning("The show is SOLD OUT!");
                return;
            }

            if ( nr <= 0 ){
                Utils.showWarning("Numar invalid de bilete!");
                return;
            }
            if( nr > Integer.parseInt(selected.getNrBileteRamase())){
                Utils.showWarning("Nu exista atatea bilete disponibile!");
                return;
            }

            Vanzare v = new Vanzare(serviceUser.getLastFreeIdVanzari() + 1,client,selected.getMeci(),nr);
            serviceUser.addVanzare(v);

            double pret = v.getMeci().getPret()*nr;
            String message = "Your total price will be: " + pret;
            Utils.showInformation(message);
        } catch (NumberFormatException x){
            Utils.showWarning("Numarul de bilete trebuie sa fie numar!");
        } catch (IndexOutOfBoundsException x){
            Utils.showWarning(x.getMessage());
        }

        clearClientFields();
        buttonRefreshClicked();
    }

    @FXML
    public void buttonFilterClicked(){
        model.setAll(getAvailableMeciuri());
    }


    /// UTILS
    public ObservableList<MeciDTO> getMeciuri() {
        Stream<Meci> s = StreamSupport.stream(serviceUser.getAllMeciuri().spliterator(), false);
        return s.map(x -> new MeciDTO(x, serviceUser.getTotalBileteCumparateForMeci(x)))
                .collect(Collectors.collectingAndThen(toList(), l -> FXCollections.observableArrayList(l)));
    }

    public ObservableList<MeciDTO> getAvailableMeciuri() {
        Stream<Meci> s = StreamSupport.stream(serviceUser.getAllMeciuri().spliterator(), false);
        return s.map(x -> new MeciDTO(x, serviceUser.getTotalBileteCumparateForMeci(x)))
                .filter(x -> !x.getNrBileteRamase().equals("SOLD OUT!"))
                .sorted(Comparator.comparingInt(x -> Integer.parseInt(x.getNrBileteRamase())))
                .collect(Collectors.collectingAndThen(toList(), l -> FXCollections.observableArrayList(l)));
    }


    public String getMeciDTOInfo(MeciDTO m){
        String aux = "About the first team:\n\n";
        aux += "Name: " + m.getEchipa1().getNume() + "\n";
        aux += "From: " + m.getEchipa1().getOras() + "\n";
        aux += "Number of players: " + m.getEchipa1().getNrJucatori() + "\n\n";

        aux += "About the second team:\n\n";
        aux += "Name: " + m.getEchipa2().getNume() + "\n";
        aux += "From: " + m.getEchipa2().getOras() + "\n";
        aux += "Number of players: " + m.getEchipa2().getNrJucatori() + "\n\n";

        aux += "More info about the match:\n\n";
        aux += "Current score: " + m.getScor() + "\n";
        aux += "Price for one ticket: " + m.getPret() + "\n";
        aux += "Number of tickets in total: " + m.getNrBilete()  + "\n";
        aux += "Number of tickets left: " + m.getNrBileteRamase() + "\n";
        return aux;
    }


    @Override
    public void update() {
        buttonRefreshClicked();
    }
}
