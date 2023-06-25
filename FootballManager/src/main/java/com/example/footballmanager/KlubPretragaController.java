package com.example.footballmanager;

import baza.BazaPodataka;
import entitet.Klub;
import iznimke.BazaPodatakaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KlubPretragaController {

    List<Klub> klubovi = BazaPodataka.dohvatiKlubove();

    @FXML
    private TextField nazivKlub;
    @FXML
    private TextField drzavaKlub;
    @FXML
    private TextField trenerKlub;
    @FXML
    private TextField stadionKlub;

    @FXML
    private TableView<Klub> klubTableView;

    @FXML
    private TableColumn<Klub, String> nazivTableColumn;
    @FXML
    private TableColumn<Klub, String> drzavaTableColumn;
    @FXML
    private TableColumn<Klub, String> trenerTableColumn;
    @FXML
    private TableColumn<Klub, String> stadionTableColumn;

    public KlubPretragaController() throws BazaPodatakaException {
    }

    @FXML
    private void initialize() {

        nazivTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getNazivKluba());
        });

        drzavaTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getDrzava());
        });

        trenerTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getTrener().getIme() + " " + cellData.getValue().getTrener().getPrezime());
        });

        stadionTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getStadion().nazivStadiona());
        });

        ObservableList<Klub> klubObservableList = FXCollections.observableList(klubovi);
        klubTableView.setItems(klubObservableList);
    }

    @FXML
    protected void onSearchButtonClick() {
        String uneseniNaziv = nazivKlub.getText();
        String unesenaDrzava = drzavaKlub.getText();
        String uneseniTrener = trenerKlub.getText();
        String uneseniStadion = stadionKlub.getText();

        List<Klub> filteredList = new ArrayList<>(klubovi);

        filteredList = filteredList.stream()
                .filter(klub ->
                        klub.getNazivKluba().toLowerCase().contains(uneseniNaziv) &&
                                klub.getDrzava().toLowerCase().contains(unesenaDrzava) &&
                                (klub.getTrener().getIme() + " " + klub.getTrener().getPrezime()).toLowerCase().contains(uneseniTrener) &&
                                klub.getStadion().nazivStadiona().toLowerCase().contains(uneseniStadion))
                .collect(Collectors.toList());

        klubTableView.setItems(FXCollections.observableList(filteredList));
    }

    @FXML
    public void prikaziIgracePoKlubu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("igraci-klub.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Igrači kluba");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    @FXML
    public void prikaziIgracePoKlubuUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("igraci-klub-user.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Igrači kluba");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
}
