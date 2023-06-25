package com.example.footballmanager;

import baza.BazaPodataka;
import entitet.Serijalizacija;
import entitet.Stadion;
import iznimke.BazaPodatakaException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class StadionUnosController {

    private static final Logger logger = LoggerFactory.getLogger(StadionUnosController.class);

    List<Stadion> stadioni = BazaPodataka.dohvatiStadione();

    @FXML
    private TextField nazivStadionTextField;
    @FXML
    private TextField kapacitetStadionTextField;

    public StadionUnosController() throws BazaPodatakaException {
    }

    @FXML
    protected void onSubmitButtonClick() throws BazaPodatakaException, IOException {
        StringBuilder errorMessage = new StringBuilder();

        Long id = (long) stadioni.size() + 1;
        String uneseniNaziv = nazivStadionTextField.getText();
        String uneseniKapacitet = kapacitetStadionTextField.getText();

        if (uneseniNaziv.isEmpty()) {
            errorMessage.append("Potrebno je unijeti naziv stadiona!\n");
        }
        if (uneseniKapacitet.isEmpty()) {
            errorMessage.append("Potrebno je unijeti kapacitet stadiona!\n");
        }

        if (errorMessage.isEmpty()) {
            Stadion noviStadion = null;

            try{
                noviStadion = new Stadion(
                        id,
                        uneseniNaziv,
                        Integer.parseInt(uneseniKapacitet)
                );
            } catch (NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Spremanje neuspješno!");
                alert.setHeaderText("Za kapacitet treba unijeti broj!");
                alert.setContentText(errorMessage.toString());
                alert.showAndWait();

                logger.error("Unešen String umjesto broja za kapacitet", ex);

                throw new NumberFormatException("Unešen String umjesto broja za kapacitet");
            }

            try {
                BazaPodataka.spremiNoviStadion(noviStadion);
            } catch (BazaPodatakaException ex) {
                String poruka = "Došlo je do pogreške u radu s bazom podataka";
                throw new BazaPodatakaException(poruka, ex);
            }

            List<String> stadioniSerijalizacijaList;
            StringBuilder azuriranje = new StringBuilder();
            Serijalizacija<String> serijalizacija = new Serijalizacija<>();
            stadioniSerijalizacijaList = serijalizacija.deserijaliziraj("dat//stadioniSerijalizacija.bin");
            azuriranje.append("Novi stadion " + uneseniNaziv
                    + ", kapacitet " + uneseniKapacitet
                    + " unio admin " + LocalDateTime.now() + "\n");
            stadioniSerijalizacijaList.add(azuriranje.toString());
            serijalizacija.serijaliziraj(stadioniSerijalizacijaList,"dat//stadioniSerijalizacija.bin");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Igrač uspješno spremljen!");
            alert.setHeaderText("Spremljeno!");
            alert.setContentText("Stadion " + uneseniNaziv + " spremljen!");
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
