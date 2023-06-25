package com.example.footballmanager;

import baza.BazaPodataka;
import entitet.Igrac;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IgracAzuriranjeController {
    private static final Logger logger = LoggerFactory.getLogger(IgracAzuriranjeController.class);

    List<Igrac> igraci = BazaPodataka.dohvatiIgrace();
    List<Klub> klubovi = BazaPodataka.dohvatiKlubove();

    @FXML
    private TextField imeIgrac;
    @FXML
    private TextField prezimeIgrac;
    @FXML
    private DatePicker datumRodenjaIgrac;
    @FXML
    private TextField vrijednostIgrac;
    @FXML
    private ComboBox<String> pozicijaIgrac;
    @FXML
    private TextField drzavljanstvoIgrac;
    @FXML
    private ComboBox<Klub> klubIgrac;

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

    public IgracAzuriranjeController() throws BazaPodatakaException {
    }

    @FXML
    private void initialize() {

        ArrayList<String> pozicije = new ArrayList<>();
        pozicije.add("GK");
        pozicije.add("CB");
        pozicije.add("LB");
        pozicije.add("LWB");
        pozicije.add("RB");
        pozicije.add("RWB");
        pozicije.add("CDM");
        pozicije.add("CM");
        pozicije.add("CAM");
        pozicije.add("LW");
        pozicije.add("RW");
        pozicije.add("SS");
        pozicije.add("ST");

        pozicijaIgrac.getItems().addAll(pozicije);

        klubIgrac.setItems(FXCollections.observableList(klubovi));

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
    protected void onSearchButtonClick() throws IOException {
        TableView.TableViewSelectionModel<Igrac> selectionModel =
                igracTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        List<String> igraciSerijalizacijaList;
        StringBuilder azuriranje = new StringBuilder();
        Serijalizacija<String> serijalizacija = new Serijalizacija<>();
        igraciSerijalizacijaList = serijalizacija.deserijaliziraj("dat//igraciSerijalizacija.bin");
        azuriranje.append("Staru vrijednost " + selectionModel.getSelectedItem().toString()
                + ", datum rođenja " + selectionModel.getSelectedItem().getDatumRodenja().toString()
                + ", vrijednost " + selectionModel.getSelectedItem().getVrijednost().toString()
                + ", pozicija " + selectionModel.getSelectedItem().getPozicija()
                + ", državljanstvo " + selectionModel.getSelectedItem().getDrzavljanstvo()
                + " promijenio admin " + LocalDateTime.now() + "\n");

        StringBuilder errorMessage = new StringBuilder();

        String unesenoIme = imeIgrac.getText();
        String unesenoPrezime = prezimeIgrac.getText();
        LocalDate uneseniDatum = datumRodenjaIgrac.getValue();
        String unesenaVrijednost = vrijednostIgrac.getText();
        String unesenaPozicija = pozicijaIgrac.getValue();
        String unesenoDrzavljanstvo = drzavljanstvoIgrac.getText();
        String unesenKlubNaziv = klubIgrac.getValue().getNazivKluba();

        if (unesenoIme.isEmpty()) {
            errorMessage.append("Potrebno je unijeti ime igrača!\n");
        }
        if (unesenoPrezime.isEmpty()) {
            errorMessage.append("Potrebno je unijeti prezime igrača!\n");
        }
        if (uneseniDatum == null) {
            errorMessage.append("Potrebno je odabrati datum rođenja igrača!\n");
        }
        if (unesenaVrijednost.isEmpty()) {
            errorMessage.append("Potrebno je unijeti vrijednost igrača!\n");
        }
        if (unesenaPozicija.isEmpty()) {
            errorMessage.append("Potrebno je odabrati poziciju igrača!\n");
        }
        if (unesenoDrzavljanstvo.isEmpty()) {
            errorMessage.append("Potrebno je unijeti nacionalnost igrača!");
        }
        if (unesenKlubNaziv.isEmpty()) {
            errorMessage.append("Potrebno je odabrati klub igrača!");
        }

        if (errorMessage.isEmpty()) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Ažurirati igrača?");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Sigurno želite ažurirati igrača?");

            Optional<ButtonType> result = confirmation.showAndWait();

            if (result.get() == ButtonType.OK) {

                try{
                    BazaPodataka.azurirajIgraca(selectionModel.getSelectedItem(), unesenoIme, unesenoPrezime, uneseniDatum, unesenaVrijednost,
                            unesenaPozicija, unesenoDrzavljanstvo, unesenKlubNaziv);
                } catch (BazaPodatakaException ex){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Spremanje neuspješno!");
                    alert.setHeaderText("Za vrijednost treba unijeti broj!");
                    alert.showAndWait();

                    throw new BazaPodatakaException("Unešen String umjesto broja za vrijednost");
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Igrač uspješno ažuriran!");
                alert.setHeaderText("Ažurirano!");
                alert.setContentText("Igrač " + selectionModel.getSelectedItem().getIme() + " " + selectionModel.getSelectedItem().getPrezime() + " je ažuriran!");
                alert.showAndWait();

                Platform.runLater(new OsvjeziIgraceNit(igracTableView));
            }
        }

        try {
            Path putanja = Path.of("dat//igraciSerijalizacija.bin");
            if(Files.exists(putanja)){
                Files.delete(putanja);
            }
        } catch (IOException ex) {
            logger.error("Datoteka nije pronađena", ex);

            throw new RuntimeException(ex);
        }

        azuriranje.append("Novu vrijednost " + unesenoIme + " " + unesenoPrezime
                + ", datum rođenja " + uneseniDatum.toString()
                + ", vrijednost " + unesenaVrijednost
                + ", pozicija " + unesenaPozicija
                + ", državljanstvo " + unesenoDrzavljanstvo
                + " promijenio admin " + LocalDateTime.now() + "\n");
        igraciSerijalizacijaList.add(azuriranje.toString());
        serijalizacija.serijaliziraj(igraciSerijalizacijaList,"dat//igraciSerijalizacija.bin");
    }

    @FXML
    protected void onChooseButtonClick() throws BazaPodatakaException {
        TableView.TableViewSelectionModel<Igrac> selectionModel =
                igracTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        imeIgrac.setText(selectionModel.getSelectedItem().getIme());
        prezimeIgrac.setText(selectionModel.getSelectedItem().getPrezime());
        datumRodenjaIgrac.setValue(selectionModel.getSelectedItem().getDatumRodenja());
        vrijednostIgrac.setText(selectionModel.getSelectedItem().getVrijednost().toString());
        pozicijaIgrac.setValue(selectionModel.getSelectedItem().getPozicija());
        drzavljanstvoIgrac.setText(selectionModel.getSelectedItem().getDrzavljanstvo());
        klubIgrac.setValue(BazaPodataka.getKlub(selectionModel.getSelectedItem().getKlubId()));
    }
}
