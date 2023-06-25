package com.example.footballmanager;

import baza.BazaPodataka;
import entitet.Serijalizacija;
import entitet.Stadion;
import iznimke.BazaPodatakaException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import niti.OsvjeziIgraceNit;
import niti.OsvjeziStadioneNit;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class StadionBrisanjeController {

    List<Stadion> stadioni = BazaPodataka.dohvatiStadione();

    @FXML
    private TableView<Stadion> stadionTableView;

    @FXML
    private TableColumn<Stadion, String> nazivTableColumn;
    @FXML
    private TableColumn<Stadion, String> kapacitetTableColumn;



    public StadionBrisanjeController() throws BazaPodatakaException {
    }

    @FXML
    public void initialize() {

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
        TableView.TableViewSelectionModel<Stadion> selectionModel =
                stadionTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        try {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Obrisati stadion?");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Sigurno želite obrisati stadion?");

            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.get() == ButtonType.OK) {
                List<String> stadioniSerijalizacijaList;
                StringBuilder azuriranje = new StringBuilder();
                Serijalizacija<String> serijalizacija = new Serijalizacija<>();
                stadioniSerijalizacijaList = serijalizacija.deserijaliziraj("dat//stadioniSerijalizacija.bin");
                azuriranje.append("Stadion " + selectionModel.getSelectedItem().toString()
                        + ", kapacitet " + selectionModel.getSelectedItem().kapacitet()
                        + " izbrisao admin " + LocalDateTime.now() + "\n");
                stadioniSerijalizacijaList.add(azuriranje.toString());
                serijalizacija.serijaliziraj(stadioniSerijalizacijaList,"dat//stadioniSerijalizacija.bin");

                BazaPodataka.obrisiStadion(selectionModel.getSelectedItem());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Igrač uspješno izbrisan!");
                alert.setHeaderText("Izbrisano!");
                alert.setContentText("Stadion " + selectionModel.getSelectedItem().nazivStadiona() + " je izbrisan!");
                alert.showAndWait();

                Platform.runLater(new OsvjeziStadioneNit(stadionTableView));
            } else {

            }

        } catch (BazaPodatakaException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Neuspješno spremanje!");
            alert.setHeaderText("Nije spremljeno!");
            alert.setContentText(e.toString());

            alert.showAndWait();
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
