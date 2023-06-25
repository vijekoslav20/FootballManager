package com.example.footballmanager;

import baza.BazaPodataka;
import entitet.*;
import iznimke.BazaPodatakaException;
import iznimke.StadionKoristenDvaputException;
import iznimke.ZauzetiTrenerException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class KlubUnosController {
    List<Klub> klubovi = BazaPodataka.dohvatiKlubove();
    List<Trener> treneri = BazaPodataka.dohvatiTrenere();
    List<Stadion> stadioni = BazaPodataka.dohvatiStadione();

    @FXML
    private TextField nazivKlubTextField;
    @FXML
    private TextField drzavaKlubTextField;
    @FXML
    private ComboBox<Trener> trenerKlubComboBox;
    @FXML
    private ComboBox<Stadion> stadionKlubComboBox;

    public KlubUnosController() throws BazaPodatakaException {
    }

    @FXML
    private void initialize() {

        trenerKlubComboBox.setItems(FXCollections.observableList(treneri));

        stadionKlubComboBox.setItems(FXCollections.observableList(stadioni));

    }

    @FXML
    protected void onSubmitButtonClick() throws BazaPodatakaException, IOException {
        StringBuilder errorMessage = new StringBuilder();

        Long id = (long) klubovi.size() + 1;
        String uneseniNaziv = nazivKlubTextField.getText();
        String unesenaDrzava = drzavaKlubTextField.getText();
        Trener uneseniTrener = trenerKlubComboBox.getValue();
        Stadion uneseniStadion = stadionKlubComboBox.getValue();

        if(uneseniNaziv.isEmpty()) {
            errorMessage.append("Potrebno je unijeti naziv kluba!\n");
        }
        if(unesenaDrzava.isEmpty()) {
            errorMessage.append("Potrebno je unijeti državu kluba!\n");
        }
        if(uneseniTrener == null) {
            errorMessage.append("Potrebno je odabrati trenera kluba!\n");
        }
        if(uneseniStadion == null) {
            errorMessage.append("Potrebno je odabrati stadion kluba!\n");
        }

        if (errorMessage.isEmpty()) {

            Trener trener = treneri.stream()
                    .filter(tren -> (tren.toString()).equals(uneseniTrener.toString()))
                    .findFirst().orElse(null);

            Stadion stadion = stadioni.stream()
                    .filter(stad -> stad.nazivStadiona().equals(uneseniStadion.nazivStadiona()))
                    .findFirst().orElse(null);

            Klub noviKlub = new Klub(
                    id,
                    uneseniNaziv,
                    unesenaDrzava,
                    trener,
                    stadion,
                    new ArrayList<Igrac>()
            );

            try {
                noviKlub.provjeraTrenera(trener);
            } catch (ZauzetiTrenerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Spremanje neuspješno!");
                alert.setHeaderText("Trener već trenira drugi klub!");
                alert.setContentText(errorMessage.toString());
                alert.showAndWait();

                throw new RuntimeException(e);
            }

            try {
                noviKlub.provjeraStadiona(stadion);
            } catch (StadionKoristenDvaputException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Spremanje neuspješno!");
                alert.setHeaderText("Već dva kluba koriste ovaj stadion!");
                alert.setContentText(errorMessage.toString());
                alert.showAndWait();

                throw new RuntimeException(e);
            }

            try {
                BazaPodataka.spremiNoviKlub(noviKlub);
            } catch (BazaPodatakaException ex) {
                String poruka = "Došlo je do pogreške u radu s bazom podataka";
                throw new BazaPodatakaException(poruka, ex);
            }

            List<String> kluboviSerijalizacijaList;
            StringBuilder azuriranje = new StringBuilder();
            Serijalizacija<String> serijalizacija = new Serijalizacija<>();
            kluboviSerijalizacijaList = serijalizacija.deserijaliziraj("dat//kluboviSerijalizacija.bin");
            azuriranje.append("Novi klub " + uneseniNaziv
                    + ", iz države " + unesenaDrzava
                    + ", trener " + trener.toString()
                    + ", stadion " + stadion.toString()
                    + " unio admin " + LocalDateTime.now() + "\n");
            kluboviSerijalizacijaList.add(azuriranje.toString());
            serijalizacija.serijaliziraj(kluboviSerijalizacijaList,"dat//kluboviSerijalizacija.bin");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Klub uspješno spremljen!");
            alert.setHeaderText("Spremljeno!");
            alert.setContentText("Klub " + uneseniNaziv + " spremljen!");
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
