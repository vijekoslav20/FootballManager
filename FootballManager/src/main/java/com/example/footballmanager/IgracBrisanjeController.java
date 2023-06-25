package com.example.footballmanager;

import baza.BazaPodataka;
import entitet.Igrac;
import entitet.Serijalizacija;
import iznimke.BazaPodatakaException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import niti.OsvjeziIgraceNit;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class IgracBrisanjeController {

    List<Igrac> igraci = BazaPodataka.dohvatiIgrace();

    @FXML
    private TableView<Igrac> igracTableView;

    @FXML
    private TableColumn<Igrac, String> imeTableColumn;
    @FXML
    private TableColumn<Igrac, String> prezimeTableColumn;
    @FXML
    private TableColumn<Igrac, String> datumRodenjaTableColumn;
    @FXML
    private TableColumn<Igrac, String> vrijednostTableColumn;
    @FXML
    private TableColumn<Igrac, String> pozicijaTableColumn;
    @FXML
    private TableColumn<Igrac, String> drzavljanstvoTableColumn;



    public IgracBrisanjeController() throws BazaPodatakaException {
    }

    @FXML
    public void initialize() {

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

        vrijednostTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getVrijednost().toString());
        });

        pozicijaTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getPozicija());
        });

        drzavljanstvoTableColumn.setCellValueFactory(cellData -> {
            return new SimpleStringProperty(cellData.getValue().getDrzavljanstvo());
        });

        ObservableList<Igrac> igracObservableList = FXCollections.observableList(igraci);
        igracTableView.setItems(igracObservableList);
    }

    @FXML
    protected void onSearchButtonClick() {
        TableView.TableViewSelectionModel<Igrac> selectionModel =
                igracTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        try {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Obrisati igrača?");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Sigurno želite obrisati igrača?");

            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.get() == ButtonType.OK) {
                List<String> igraciSerijalizacijaList;
                StringBuilder azuriranje = new StringBuilder();
                Serijalizacija<String> serijalizacija = new Serijalizacija<>();
                igraciSerijalizacijaList = serijalizacija.deserijaliziraj("dat//igraciSerijalizacija.bin");
                azuriranje.append("Igrača " + selectionModel.getSelectedItem().toString()
                        + ", datum rođenja " + selectionModel.getSelectedItem().getDatumRodenja().toString()
                        + ", vrijednost " + selectionModel.getSelectedItem().getVrijednost().toString()
                        + ", pozicija " + selectionModel.getSelectedItem().getPozicija()
                        + ", državljanstvo " + selectionModel.getSelectedItem().getDrzavljanstvo()
                        + " izbrisao admin " + LocalDateTime.now() + "\n");
                igraciSerijalizacijaList.add(azuriranje.toString());
                serijalizacija.serijaliziraj(igraciSerijalizacijaList,"dat//igraciSerijalizacija.bin");

                BazaPodataka.obrisiIgraca(selectionModel.getSelectedItem());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Igrač uspješno izbrisan!");
                alert.setHeaderText("Izbrisano!");
                alert.setContentText("Igrač " + selectionModel.getSelectedItem().getIme() + " " + selectionModel.getSelectedItem().getPrezime() + " je izbrisan!");
                alert.showAndWait();

                Platform.runLater(new OsvjeziIgraceNit(igracTableView));
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
