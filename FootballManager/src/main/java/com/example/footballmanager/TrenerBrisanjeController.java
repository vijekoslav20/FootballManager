package com.example.footballmanager;

import baza.BazaPodataka;
import entitet.Serijalizacija;
import entitet.Trener;
import iznimke.BazaPodatakaException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import niti.OsvjeziIgraceNit;
import niti.OsvjeziTrenereNit;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class TrenerBrisanjeController {

    List<Trener> treneri = BazaPodataka.dohvatiTrenere();

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



    public TrenerBrisanjeController() throws BazaPodatakaException {
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
        TableView.TableViewSelectionModel<Trener> selectionModel =
                trenerTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        try {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Obrisati trenera?");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Sigurno želite obrisati trenera?");

            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.get() == ButtonType.OK) {
                List<String> treneriSerijalizacijaList;
                StringBuilder azuriranje = new StringBuilder();
                Serijalizacija<String> serijalizacija = new Serijalizacija<>();
                treneriSerijalizacijaList = serijalizacija.deserijaliziraj("dat//treneriSerijalizacija.bin");
                azuriranje.append("Trenera " + selectionModel.getSelectedItem().toString()
                        + ", datum rođenja " + selectionModel.getSelectedItem().getDatumRodenja().toString()
                        + ", državljanstvo " + selectionModel.getSelectedItem().getDrzavljanstvo()
                        + ", omiljena formacija " + selectionModel.getSelectedItem().getOmiljenaFormacija()
                        + " izbrisao admin " + LocalDateTime.now() + "\n");
                treneriSerijalizacijaList.add(azuriranje.toString());
                serijalizacija.serijaliziraj(treneriSerijalizacijaList,"dat//treneriSerijalizacija.bin");

                BazaPodataka.obrisiTrenera(selectionModel.getSelectedItem());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Trener uspješno izbrisan!");
                alert.setHeaderText("Izbrisano!");
                alert.setContentText("Trener " + selectionModel.getSelectedItem().getIme() + " " + selectionModel.getSelectedItem().getPrezime() + " je izbrisan!");
                alert.showAndWait();

                Platform.runLater(new OsvjeziTrenereNit(trenerTableView));
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
