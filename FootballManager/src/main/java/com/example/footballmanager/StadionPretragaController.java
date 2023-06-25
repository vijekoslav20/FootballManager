package com.example.footballmanager;

import baza.BazaPodataka;
import entitet.Stadion;
import iznimke.BazaPodatakaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StadionPretragaController {

    List<Stadion> stadioni = BazaPodataka.dohvatiStadione();

    @FXML
    private TextField nazivStadion;
    @FXML
    private TextField kapacitetStadion;

    @FXML
    private TableView<Stadion> stadionTableView;

    @FXML
    private TableColumn<Stadion, String> nazivTableColumn;
    @FXML
    private TableColumn<Stadion, String> kapacitetTableColumn;

    public StadionPretragaController() throws BazaPodatakaException {
    }

    @FXML
    private void initialize() {

        nazivTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().nazivStadiona());
        });

        kapacitetTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().kapacitet().toString());
        });

        ObservableList<Stadion> stadionObservableList = FXCollections.observableList(stadioni);
        stadionTableView.setItems(stadionObservableList);
    }

    @FXML
    protected void onSearchButtonClick() {
        String uneseniNaziv = nazivStadion.getText();
        String uneseniKapacitet = kapacitetStadion.getText();

        List<Stadion> filteredList = new ArrayList<>(stadioni);

        filteredList = filteredList.stream()
                .filter(stadion ->
                        stadion.nazivStadiona().toLowerCase().contains(uneseniNaziv))
                .filter(stadion -> stadion.kapacitet() >= Integer.parseInt(uneseniKapacitet))
                .collect(Collectors.toList());

        stadionTableView.setItems(FXCollections.observableList(filteredList));
    }
}
