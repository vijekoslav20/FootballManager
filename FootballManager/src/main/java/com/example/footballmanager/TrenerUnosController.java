package com.example.footballmanager;

import baza.BazaPodataka;
import entitet.Serijalizacija;
import entitet.Trener;
import iznimke.BazaPodatakaException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TrenerUnosController {

    List<Trener> treneri = BazaPodataka.dohvatiTrenere();

    @FXML
    private TextField imeTrenerTextField;
    @FXML
    private TextField prezimeTrenerTextField;
    @FXML
    private DatePicker datumRodenjaTrenerDatePicker;
    @FXML
    private TextField drzavljanstvoTrenerTextField;
    @FXML
    private ComboBox<String> omiljenaFormacijaTrenerComboBox;

    public TrenerUnosController() throws BazaPodatakaException {
    }

    @FXML
    private void initialize() {

        ArrayList<String> formacije = new ArrayList<>();
        formacije.add("4-2-3-1");
        formacije.add("4-3-3");

        omiljenaFormacijaTrenerComboBox.getItems().addAll(formacije);
    }

    @FXML
    protected void onSubmitButtonClick() throws BazaPodatakaException, IOException {
        StringBuilder errorMessage = new StringBuilder();

        Long id = (long) treneri.size() + 1;
        String unesenoIme = imeTrenerTextField.getText();
        String unesenoPrezime = prezimeTrenerTextField.getText();
        LocalDate uneseniDatum = datumRodenjaTrenerDatePicker.getValue();
        String unesenoDrzavljanstvo = drzavljanstvoTrenerTextField.getText();
        String unesenaFormacija = omiljenaFormacijaTrenerComboBox.getValue();

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
            errorMessage.append("Potrebno je unijeti nacionalnost trenera!\n");
        }
        if (unesenaFormacija == null) {
            errorMessage.append("Potrebno je odabrati omiljenu formaciju trenera!\n");
        }

        if (errorMessage.isEmpty()) {
            Trener noviTrener = new Trener(
                    id,
                    unesenoIme,
                    unesenoPrezime,
                    uneseniDatum,
                    unesenoDrzavljanstvo,
                    unesenaFormacija
            );

            try {
                BazaPodataka.spremiNovogTrenera(noviTrener);
            } catch (BazaPodatakaException ex) {
                String poruka = "Došlo je do pogreške u radu s bazom podataka";
                throw new BazaPodatakaException(poruka, ex);
            }

            List<String> treneriSerijalizacijaList;
            StringBuilder azuriranje = new StringBuilder();
            Serijalizacija<String> serijalizacija = new Serijalizacija<>();
            treneriSerijalizacijaList = serijalizacija.deserijaliziraj("dat//treneriSerijalizacija.bin");
            azuriranje.append("Novog trenera " + unesenoIme + " " + unesenoPrezime
                    + ", datum rođenja " + uneseniDatum.toString()
                    + ", državljanstvo " + unesenoDrzavljanstvo
                    + ", omiljena formacija " + unesenaFormacija
                    + " unio admin " + LocalDateTime.now() + "\n");
            treneriSerijalizacijaList.add(azuriranje.toString());
            serijalizacija.serijaliziraj(treneriSerijalizacijaList,"dat//treneriSerijalizacija.bin");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Igrač uspješno spremljen!");
            alert.setHeaderText("Spremljeno!");
            alert.setContentText("Trener " + unesenoIme + " " + unesenoPrezime + " spremljen!");
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
