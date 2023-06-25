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
import niti.OsvjeziTrenereNit;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TrenerAzuriranjeController {

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

    public TrenerAzuriranjeController() throws BazaPodatakaException {
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
    protected void onSearchButtonClick() throws IOException {
        TableView.TableViewSelectionModel<Trener> selectionModel =
                trenerTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        List<String> treneriSerijalizacijaList;
        StringBuilder azuriranje = new StringBuilder();
        Serijalizacija<String> serijalizacija = new Serijalizacija<>();
        treneriSerijalizacijaList = serijalizacija.deserijaliziraj("dat//treneriSerijalizacija.bin");
        azuriranje.append("Staru vrijednost " + selectionModel.getSelectedItem().toString()
                + ", datum rođenja " + selectionModel.getSelectedItem().getDatumRodenja().toString()
                + ", državljanstvo " + selectionModel.getSelectedItem().getDrzavljanstvo()
                + ", omiljena formacija " + selectionModel.getSelectedItem().getOmiljenaFormacija()
                + " promijenio admin " + LocalDateTime.now() + "\n");

        StringBuilder errorMessage = new StringBuilder();

        String unesenoIme = imeTrener.getText();
        String unesenoPrezime = prezimeTrener.getText();
        LocalDate uneseniDatum = datumRodenjaTrener.getValue();
        String unesenoDrzavljanstvo = drzavljanstvoTrener.getText();
        String unesenaFormacija = omiljenaFormacijaTrener.getValue();

        if (unesenoIme.isEmpty()) {
            errorMessage.append("Potrebno je unijeti ime trenera!\n");
        }
        if (unesenoPrezime.isEmpty()) {
            errorMessage.append("Potrebno je unijeti prezime trenera!\n");
        }
        if (uneseniDatum == null) {
            errorMessage.append("Potrebno je odabrati datum rođenja trenera!\n");
        }
        if (unesenoDrzavljanstvo.isEmpty()) {
            errorMessage.append("Potrebno je unijeti nacionalnost trenera!");
        }
        if (unesenaFormacija.isEmpty()) {
            errorMessage.append("Potrebno je odabrati omiljenu formaciju trenera!\n");
        }

        if (errorMessage.isEmpty()) {
            try {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Ažurirati trenera?");
                confirmation.setHeaderText(null);
                confirmation.setContentText("Sigurno želite ažurirati trenera?");

                Optional<ButtonType> result = confirmation.showAndWait();

                if (result.get() == ButtonType.OK) {
                    BazaPodataka.azurirajTrenera(selectionModel.getSelectedItem(), unesenoIme, unesenoPrezime, uneseniDatum, unesenoDrzavljanstvo, unesenaFormacija);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Trener uspješno ažuriran!");
                    alert.setHeaderText("Ažurirano!");
                    alert.setContentText("Trener " + selectionModel.getSelectedItem().getIme() + " " + selectionModel.getSelectedItem().getPrezime() + " je ažuriran!");
                    alert.showAndWait();

                    Platform.runLater(new OsvjeziTrenereNit(trenerTableView));
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

        azuriranje.append("Novu vrijednost " + unesenoIme + " " + unesenoPrezime
                + ", datum rođenja " + uneseniDatum.toString()
                + ", državljanstvo " + unesenoDrzavljanstvo
                + ", omiljena formacija " + unesenaFormacija
                + " promijenio admin " + LocalDateTime.now() + "\n");
        treneriSerijalizacijaList.add(azuriranje.toString());
        serijalizacija.serijaliziraj(treneriSerijalizacijaList,"dat//treneriSerijalizacija.bin");
    }

    @FXML
    protected void onChooseButtonClick() {
        TableView.TableViewSelectionModel<Trener> selectionModel =
                trenerTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        imeTrener.setText(selectionModel.getSelectedItem().getIme());
        prezimeTrener.setText(selectionModel.getSelectedItem().getPrezime());
        datumRodenjaTrener.setValue(selectionModel.getSelectedItem().getDatumRodenja());
        drzavljanstvoTrener.setText(selectionModel.getSelectedItem().getDrzavljanstvo());
        omiljenaFormacijaTrener.setValue(selectionModel.getSelectedItem().getOmiljenaFormacija());
    }
}
