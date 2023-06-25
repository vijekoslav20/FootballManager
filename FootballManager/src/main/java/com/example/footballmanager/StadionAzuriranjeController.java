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
import niti.OsvjeziStadioneNit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class StadionAzuriranjeController {

    private static final Logger logger = LoggerFactory.getLogger(StadionAzuriranjeController.class);

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

    public StadionAzuriranjeController() throws BazaPodatakaException {
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
    protected void onSearchButtonClick() throws IOException {
        TableView.TableViewSelectionModel<Stadion> selectionModel =
                stadionTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        List<String> stadioniSerijalizacijaList;
        StringBuilder azuriranje = new StringBuilder();
        Serijalizacija<String> serijalizacija = new Serijalizacija<>();
        stadioniSerijalizacijaList = serijalizacija.deserijaliziraj("dat//stadioniSerijalizacija.bin");
        azuriranje.append("Staru vrijednost " + selectionModel.getSelectedItem().toString()
                + ", kapacitet " + selectionModel.getSelectedItem().kapacitet()
                + " promijenio admin " + LocalDateTime.now() + "\n");

        StringBuilder errorMessage = new StringBuilder();

        String uneseniNaziv = nazivStadion.getText();
        String uneseniKapacitet = kapacitetStadion.getText();

        if (uneseniNaziv.isEmpty()) {
            errorMessage.append("Potrebno je unijeti ime igrača!\n");
        }
        if (uneseniKapacitet.isEmpty()) {
            errorMessage.append("Potrebno je unijeti prezime igrača!\n");
        }

        if (errorMessage.isEmpty()) {
            try {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Ažurirati stadion?");
                confirmation.setHeaderText(null);
                confirmation.setContentText("Sigurno želite ažurirati stadion?");

                Optional<ButtonType> result = confirmation.showAndWait();

                if (result.get() == ButtonType.OK) {

                    try{
                        BazaPodataka.azurirajStadion(selectionModel.getSelectedItem(), uneseniNaziv, Integer.parseInt(uneseniKapacitet));
                    } catch (NumberFormatException ex){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Spremanje neuspješno!");
                        alert.setHeaderText("Za kapacitet treba unijeti broj!");
                        alert.setContentText(errorMessage.toString());
                        alert.showAndWait();

                        logger.error("Unešen String umjesto broja za kapacitet", ex);

                        throw new NumberFormatException("Unešen String umjesto broja za kapacitet");
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Stadion uspješno ažuriran!");
                    alert.setHeaderText("Ažurirano!");
                    alert.setContentText("Stadion " + selectionModel.getSelectedItem().nazivStadiona() + " je ažuriran!");
                    alert.showAndWait();

                    Platform.runLater(new OsvjeziStadioneNit(stadionTableView));
                }
            } catch (BazaPodatakaException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Neuspješno spremanje!");
                alert.setHeaderText("Nije spremljeno!");
                alert.setContentText(e.toString());

                alert.showAndWait();
                throw new RuntimeException(e);
            }
        }

        azuriranje.append("Novu vrijednost " + uneseniNaziv
                + ", kapacitet " + uneseniKapacitet
                + " promijenio admin " + LocalDateTime.now() + "\n");
        stadioniSerijalizacijaList.add(azuriranje.toString());
        serijalizacija.serijaliziraj(stadioniSerijalizacijaList,"dat//stadioniSerijalizacija.bin");
    }

    @FXML
    protected void onChooseButtonClick() {
        TableView.TableViewSelectionModel<Stadion> selectionModel =
                stadionTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        nazivStadion.setText(selectionModel.getSelectedItem().nazivStadiona());
        kapacitetStadion.setText(selectionModel.getSelectedItem().kapacitet().toString());
    }
}
