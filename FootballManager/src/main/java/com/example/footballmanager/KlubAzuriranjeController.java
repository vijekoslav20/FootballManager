package com.example.footballmanager;

import baza.BazaPodataka;
import entitet.Klub;
import entitet.Serijalizacija;
import entitet.Stadion;
import entitet.Trener;
import iznimke.BazaPodatakaException;
import iznimke.StadionKoristenDvaputException;
import iznimke.ZauzetiTrenerException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import niti.OsvjeziKluboveNit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class KlubAzuriranjeController {

    List<Klub> klubovi = BazaPodataka.dohvatiKlubove();
    List<Trener> treneri = BazaPodataka.dohvatiTrenere();
    List<Stadion> stadioni = BazaPodataka.dohvatiStadione();

    @FXML
    private TextField nazivKlub;
    @FXML
    private TextField drzavaKlub;
    @FXML
    private ComboBox<Trener> trenerKlub;
    @FXML
    private ComboBox<Stadion> stadionKlub;

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

    public KlubAzuriranjeController() throws BazaPodatakaException {
    }

    @FXML
    private void initialize() {

        trenerKlub.setItems(FXCollections.observableList(treneri));

        stadionKlub.setItems(FXCollections.observableList(stadioni));

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
    protected void onSearchButtonClick() throws IOException {
        TableView.TableViewSelectionModel<Klub> selectionModel =
                klubTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        List<String> kluboviSerijalizacijaList;
        StringBuilder azuriranje = new StringBuilder();
        Serijalizacija<String> serijalizacija = new Serijalizacija<>();
        kluboviSerijalizacijaList = serijalizacija.deserijaliziraj("dat//kluboviSerijalizacija.bin");
        azuriranje.append("Staru vrijednost " + selectionModel.getSelectedItem().getNazivKluba()
                + ", država " + selectionModel.getSelectedItem().getDrzava()
                + ", trener " + selectionModel.getSelectedItem().getTrener().toString()
                + ", stadion " + selectionModel.getSelectedItem().getStadion().toString()
                + " promijenio admin " + LocalDateTime.now() + "\n");

        StringBuilder errorMessage = new StringBuilder();

        String uneseniNaziv = nazivKlub.getText();
        String unesenaDrzava = drzavaKlub.getText();
        String uneseniTrener = trenerKlub.getValue().toString();
        String uneseniStadion = stadionKlub.getValue().nazivStadiona();

        if (uneseniNaziv.isEmpty()) {
            errorMessage.append("Potrebno je unijeti naziv kluba!\n");
        }
        if (unesenaDrzava.isEmpty()) {
            errorMessage.append("Potrebno je unijeti državu kluba!\n");
        }
        if (uneseniTrener.isEmpty()) {
            errorMessage.append("Potrebno je odabrati trenera kluba!\n");
        }
        if (uneseniStadion.isEmpty()) {
            errorMessage.append("Potrebno je odabrati stadion kluba!\n");
        }

        if (errorMessage.isEmpty()) {

            Trener trener = treneri.stream()
                    .filter(tren -> (tren.getIme() + " " + tren.getPrezime()).equals(uneseniTrener))
                    .findFirst().orElse(null);

            Stadion stadion = stadioni.stream()
                    .filter(stad -> stad.nazivStadiona().equals(uneseniStadion))
                    .findFirst().orElse(null);

//            Klub klubZaAzuriranje = selectionModel.getSelectedItem();

            try {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Ažurirati klub?");
                confirmation.setHeaderText(null);
                confirmation.setContentText("Sigurno želite ažurirati klub?");

                Optional<ButtonType> result = confirmation.showAndWait();

                if (result.get() == ButtonType.OK) {

//                    try {
//                        klubZaAzuriranje.provjeraTreneraAzuriranje(trener, klubZaAzuriranje);
//                    } catch (ZauzetiTrenerException e) {
//                        Alert alert = new Alert(Alert.AlertType.ERROR);
//                        alert.setTitle("Spremanje neuspješno!");
//                        alert.setHeaderText("Trener već trenira drugi klub!");
//                        alert.setContentText(errorMessage.toString());
//                        alert.showAndWait();
//
//                        throw new RuntimeException(e);
//                    }
//
//                    try {
//                        klubZaAzuriranje.provjeraStadionaAzuriranje(stadion, klubZaAzuriranje);
//                    } catch (StadionKoristenDvaputException e) {
//                        Alert alert = new Alert(Alert.AlertType.ERROR);
//                        alert.setTitle("Spremanje neuspješno!");
//                        alert.setHeaderText("Već dva kluba koriste ovaj stadion!");
//                        alert.setContentText(errorMessage.toString());
//                        alert.showAndWait();
//
//                        throw new RuntimeException(e);
//                    }

                    BazaPodataka.azurirajKlub(selectionModel.getSelectedItem(), uneseniNaziv, unesenaDrzava, trener.getId(), stadion.id());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Klub uspješno ažuriran!");
                    alert.setHeaderText("Ažurirano!");
                    alert.setContentText("Klub " + selectionModel.getSelectedItem().getNazivKluba() + " je ažuriran!");
                    alert.showAndWait();

                    Platform.runLater(new OsvjeziKluboveNit(klubTableView));
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
                + ", država " + unesenaDrzava
                + ", trener " + uneseniTrener
                + ", stadion " + uneseniStadion
                + " promijenio admin " + LocalDateTime.now() + "\n");
        kluboviSerijalizacijaList.add(azuriranje.toString());
        serijalizacija.serijaliziraj(kluboviSerijalizacijaList,"dat//kluboviSerijalizacija.bin");
    }

    @FXML
    protected void onChooseButtonClick() {
        TableView.TableViewSelectionModel<Klub> selectionModel =
                klubTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        nazivKlub.setText(selectionModel.getSelectedItem().getNazivKluba());
        drzavaKlub.setText(selectionModel.getSelectedItem().getDrzava());
        trenerKlub.setValue(selectionModel.getSelectedItem().getTrener());
        stadionKlub.setValue(selectionModel.getSelectedItem().getStadion());
    }
}
