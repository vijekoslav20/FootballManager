package com.example.footballmanager;

import baza.BazaPodataka;
import entitet.Trener;
import iznimke.BazaPodatakaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TrenerPretragaController {

    List<Trener> treneri = BazaPodataka.dohvatiTrenere();

    @FXML
    private TextField imeTrener;
    @FXML
    private TextField prezimeTrener;
    @FXML
    private DatePicker datumRodenjaTrener;
    @FXML
    private TextField drzavljanstvoTrener;
    @FXML
    private ComboBox<String> omiljenaFormacijaTrener;

    @FXML
    private TableView<Trener> trenerTableView;

    @FXML
    private TableColumn<Trener, String> imeTableColumn;
    @FXML
    private TableColumn<Trener, String> prezimeTableColumn;
    @FXML
    private TableColumn<Trener, String> datumRodenjaTableColumn;
    @FXML
    private TableColumn<Trener, String> drzavljanstvoTableColumn;
    @FXML
    private TableColumn<Trener, String> omiljenaFormacijaTableColumn;

    public TrenerPretragaController() throws BazaPodatakaException {
    }

    @FXML
    private void initialize() {

        ArrayList<String> formacije = new ArrayList<>();
        formacije.add("4-2-3-1");
        formacije.add("4-3-3");

        omiljenaFormacijaTrener.getItems().addAll(formacije);

        imeTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getIme());
        });

        prezimeTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getPrezime());
        });

        datumRodenjaTableColumn.setCellValueFactory(student -> {
            SimpleStringProperty property = new SimpleStringProperty();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
            property.setValue(student.getValue().getDatumRodenja().format(formatter));

            return property;
        });

        drzavljanstvoTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getDrzavljanstvo());
        });

        omiljenaFormacijaTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getOmiljenaFormacija());
        });

        ObservableList<Trener> trenerObservableList = FXCollections.observableList(treneri);
        trenerTableView.setItems(trenerObservableList);
    }

    @FXML
    protected void onSearchButtonClick() {
        String unesenoIme = imeTrener.getText();
        String unesenoPrezime = prezimeTrener.getText();
        String uneseniDatum = Optional.ofNullable(datumRodenjaTrener.getValue())
                .map(d -> d.format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))).orElse("");
        String unesenoDrzavljanstvo = drzavljanstvoTrener.getText();
        String unesenaFormacija = omiljenaFormacijaTrener.getValue();

        List<Trener> filteredList = new ArrayList<>(treneri);

        if(omiljenaFormacijaTrener.getValue() == null){
            filteredList = filteredList.stream()
                    .filter(trener ->
                            trener.getIme().toLowerCase().contains(unesenoIme) &&
                                    trener.getPrezime().toLowerCase().contains(unesenoPrezime) &&
                                    trener.getDatumRodenja()
                                            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))
                                            .contains(uneseniDatum) &&
                                    trener.getDrzavljanstvo().toLowerCase().contains(unesenoDrzavljanstvo))
                    .collect(Collectors.toList());
        } else{
            filteredList = filteredList.stream()
                    .filter(trener ->
                            trener.getIme().toLowerCase().contains(unesenoIme) &&
                                    trener.getPrezime().toLowerCase().contains(unesenoPrezime) &&
                                    trener.getDatumRodenja()
                                            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))
                                            .contains(uneseniDatum) &&
                                    trener.getDrzavljanstvo().toLowerCase().contains(unesenoDrzavljanstvo) &&
                                    trener.getOmiljenaFormacija().contains(unesenaFormacija))
                    .collect(Collectors.toList());
        }

        trenerTableView.setItems(FXCollections.observableList(filteredList));
    }
}
