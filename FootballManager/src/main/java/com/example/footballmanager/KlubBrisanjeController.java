package com.example.footballmanager;

import baza.BazaPodataka;
import entitet.Klub;
import entitet.Serijalizacija;
import iznimke.BazaPodatakaException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import niti.OsvjeziIgraceNit;
import niti.OsvjeziKluboveNit;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class KlubBrisanjeController {

    List<Klub> klubovi = BazaPodataka.dohvatiKlubove();

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



    public KlubBrisanjeController() throws BazaPodatakaException {
    }

    @FXML
    public void initialize() {

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
        TableView.TableViewSelectionModel<Klub> selectionModel =
                klubTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        try {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Obrisati klub?");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Sigurno želite obrisati klub?");

            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.get() == ButtonType.OK) {
                List<String> kluboviSerijalizacijaList;
                StringBuilder azuriranje = new StringBuilder();
                Serijalizacija<String> serijalizacija = new Serijalizacija<>();
                kluboviSerijalizacijaList = serijalizacija.deserijaliziraj("dat//kluboviSerijalizacija.bin");
                azuriranje.append("Staru vrijednost " + selectionModel.getSelectedItem().getNazivKluba()
                        + ", država " + selectionModel.getSelectedItem().getDrzava()
                        + ", trener " + selectionModel.getSelectedItem().getTrener().toString()
                        + ", stadion " + selectionModel.getSelectedItem().getStadion().toString()
                        + " promijenio admin " + LocalDateTime.now() + "\n");
                kluboviSerijalizacijaList.add(azuriranje.toString());
                serijalizacija.serijaliziraj(kluboviSerijalizacijaList,"dat//kluboviSerijalizacija.bin");

                BazaPodataka.obrisiKlub(selectionModel.getSelectedItem());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Klub uspješno izbrisan!");
                alert.setHeaderText("Izbrisano!");
                alert.setContentText("Klub " + selectionModel.getSelectedItem().getNazivKluba() + " je izbrisan!");
                alert.showAndWait();

                Platform.runLater(new OsvjeziKluboveNit(klubTableView));
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
