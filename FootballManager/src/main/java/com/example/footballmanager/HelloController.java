package com.example.footballmanager;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class HelloController {

    public void prikaziLoginEkran() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loginEkran.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Football manager");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziPromjeneEkran() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("promjeneEkran.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 600);
        HelloApplication.getMainStage().setTitle("Promjene");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziPretraguIgraca() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("pretragaIgraci.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Pretraga igrača");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziPretraguTrenera() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("pretragaTreneri.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Pretraga trenera");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziPretraguStadiona() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("pretragaStadioni.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Pretraga stadiona");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziPretraguKlubova() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("pretragaKlubovi.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Pretraga klubova");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziPretraguIgracaUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("pretragaIgraci-user.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Pretraga igrača");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziPretraguTreneraUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("pretragaTreneri-user.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Pretraga trenera");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziPretraguStadionaUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("pretragaStadioni-user.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Pretraga stadiona");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziPretraguKlubovaUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("pretragaKlubovi-user.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Pretraga klubova");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziUnosIgraca() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("unosIgraci.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Unos igrača");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziUnosTrenera() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("unosTreneri.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Unos trenera");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziUnosStadiona() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("unosStadioni.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Unos stadiona");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziUnosKlubova() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("unosKlubovi.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Unos klubova");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziBrisanjeIgraca() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("brisanjeIgraci.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Brisanje igrača");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziBrisanjeTrenera() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("brisanjeTreneri.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Brisanje trenera");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziBrisanjeStadiona() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("brisanjeStadioni.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Brisanje stadiona");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziBrisanjeKlubova() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("brisanjeKlubovi.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Brisanje klubova");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziAzuriranjeIgraca() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("azuriranjeIgraci.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Ažuriranje igrača");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziAzuriranjeTrenera() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("azuriranjeTreneri.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Ažuriranje trenera");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziAzuriranjeStadiona() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("azuriranjeStadioni.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Ažuriranje stadiona");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziAzuriranjeKlubova() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("azuriranjeKlubovi.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Ažuriranje klubova");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziLigu() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prikazLiga.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Ligaška tablica");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void prikaziLiguUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("prikazLiga-user.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Ligaška tablica");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
}