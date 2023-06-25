package com.example.footballmanager;

import baza.BazaPodataka;
import entitet.Igrac;
import entitet.Klub;
import entitet.Serijalizacija;
import iznimke.BazaPodatakaException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IgracUnosController {

    private static final Logger logger = LoggerFactory.getLogger(IgracUnosController.class);

    List<Igrac> igraci = BazaPodataka.dohvatiIgrace();
    List<Klub> klubovi = BazaPodataka.dohvatiKlubove();

    @FXML
    private TextField imeIgracTextField;
    @FXML
    private TextField prezimeIgracTextField;
    @FXML
    private DatePicker datumRodenjaIgracDatePicker;
    @FXML
    private TextField vrijednostIgracTextField;
    @FXML
    private ComboBox<String> pozicijaIgracComboBox;
    @FXML
    private TextField drzavljanstvoIgracTextField;
    @FXML
    private ComboBox<Klub> klubIgracComboBox;

    public IgracUnosController() throws BazaPodatakaException {
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

        pozicijaIgracComboBox.getItems().addAll(pozicije);

        klubIgracComboBox.setItems(FXCollections.observableList(klubovi));
    }

    @FXML
    protected void onSubmitButtonClick() throws BazaPodatakaException, IOException {
        StringBuilder errorMessage = new StringBuilder();

        Long id = (long) igraci.size() + 1;
        String unesenoIme = imeIgracTextField.getText();
        String unesenoPrezime = prezimeIgracTextField.getText();
        LocalDate uneseniDatum = datumRodenjaIgracDatePicker.getValue();
        String unesenaVrijednost = vrijednostIgracTextField.getText();
        String unesenaPozicija = pozicijaIgracComboBox.getValue();
        String unesenoDrzavljanstvo = drzavljanstvoIgracTextField.getText();
        Klub uneseniKlub = klubIgracComboBox.getValue();

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
        if (unesenaPozicija == null) {
            errorMessage.append("Potrebno je odabrati poziciju igrača!\n");
        }
        if (unesenoDrzavljanstvo.isEmpty()) {
            errorMessage.append("Potrebno je unijeti nacionalnost igrača!\n");
        }
        if (uneseniKlub == null) {
            errorMessage.append("Potrebno je odabrati poziciju igrača!\n");
        }

        if (errorMessage.isEmpty()) {
            Klub klub = klubovi.stream()
                    .filter(k -> k.getNazivKluba().equals(uneseniKlub.getNazivKluba()))
                    .findFirst().orElse(null);

            Igrac noviIgrac = null;

            try{
                noviIgrac = new Igrac(
                        id,
                        unesenoIme,
                        unesenoPrezime,
                        uneseniDatum,
                        Integer.parseInt(unesenaVrijednost),
                        unesenaPozicija,
                        unesenoDrzavljanstvo,
                        klub.getId()
                );
            } catch (NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Spremanje neuspješno!");
                alert.setHeaderText("Za vrijednost treba unijeti broj!");
                alert.setContentText(errorMessage.toString());
                alert.showAndWait();

                logger.error("Unešen String umjesto broja za vrijednost", ex);

                throw new NumberFormatException("Unešen String umjesto broja za vrijednost");
            }

            try {
                BazaPodataka.spremiNovogIgraca(noviIgrac);
            } catch (BazaPodatakaException ex) {
                String poruka = "Došlo je do pogreške u radu s bazom podataka";
                throw new BazaPodatakaException(poruka, ex);
            }

            List<String> igraciSerijalizacijaList;
            StringBuilder azuriranje = new StringBuilder();
            Serijalizacija<String> serijalizacija = new Serijalizacija<>();
            igraciSerijalizacijaList = serijalizacija.deserijaliziraj("dat//igraciSerijalizacija.bin");
            azuriranje.append("Novog igrača " + unesenoIme + " " + unesenoPrezime
                    + ", datum rođenja " + uneseniDatum.toString()
                    + ", vrijednost " + unesenaVrijednost
                    + ", pozicija " + unesenaPozicija
                    + ", državljanstvo " + unesenoDrzavljanstvo
                    + " unio admin " + LocalDateTime.now() + "\n");
            igraciSerijalizacijaList.add(azuriranje.toString());
            serijalizacija.serijaliziraj(igraciSerijalizacijaList,"dat//igraciSerijalizacija.bin");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Igrač uspješno spremljen!");
            alert.setHeaderText("Spremljeno!");
            alert.setContentText("Igrač " + unesenoIme + " " + unesenoPrezime + " spremljen!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Spremanje uspješno!");
            alert.setHeaderText("Nije spremljeno!");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
        }
    }
}
