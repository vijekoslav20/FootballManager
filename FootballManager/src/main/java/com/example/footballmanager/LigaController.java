package com.example.footballmanager;

import baza.BazaPodataka;
import entitet.Liga;
import iznimke.BazaPodatakaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sortiranje.LigaSorter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LigaController {
    List<Liga> ligaPozicije = BazaPodataka.dohvatiLige();

    @FXML
    private TableView<Liga> ligaTableView;

    @FXML
    private TableColumn<Liga, String> klubTableColumn;
    @FXML
    private TableColumn<Liga, String> bodoviTableColumn;
    @FXML
    private TableColumn<Liga, String> odigranoTableColumn;
    @FXML
    private TableColumn<Liga, String> zabijenoTableColumn;
    @FXML
    private TableColumn<Liga, String> primljenoTableColumn;


    public LigaController() throws BazaPodatakaException {
    }

    @FXML
    private void initialize() {

        klubTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getKlub().getNazivKluba());
        });

        bodoviTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getBrojBodova().toString());
        });

        odigranoTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getOdigranoUtakmica().toString());
        });

        zabijenoTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getZabijenoGolova().toString());
        });

        primljenoTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getPrimljenoGolova().toString());
        });

        List<Liga> sortiraniKluboviLige = ligaPozicije;

        sortiraniKluboviLige = sortiraniKluboviLige.stream()
                .sorted((k1, k2)-> k2.getBrojBodova().compareTo(k1.getBrojBodova()))
                .collect(Collectors.toList());

        ObservableList<Liga> ligaObservableList = FXCollections.observableList(sortiraniKluboviLige);
        ligaTableView.setItems(ligaObservableList);
    }
}
