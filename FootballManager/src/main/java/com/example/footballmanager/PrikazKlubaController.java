package com.example.footballmanager;

import baza.BazaPodataka;
import entitet.Igrac;
import entitet.Klub;
import iznimke.BazaPodatakaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PrikazKlubaController {

    private static final Logger logger = LoggerFactory.getLogger(PrikazKlubaController.class);

    List<Igrac> igraci = BazaPodataka.dohvatiIgrace();
    List<Klub> klubovi = BazaPodataka.dohvatiKlubove();

    @FXML
    private ComboBox<Klub> klubComboBox;

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

    public PrikazKlubaController() throws BazaPodatakaException {
    }

    @FXML
    private void initialize() {

        klubComboBox.setItems(FXCollections.observableList(klubovi));

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
    protected void onSearchButtonClick() throws BazaPodatakaException {

        String odabraniKlub = null;

        try{
            odabraniKlub = klubComboBox.getValue().toString();
        } catch(NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Prikaz momčadi nije uspio");
            alert.setHeaderText("Odaberite klub!");
            alert.showAndWait();

            throw new NullPointerException("Nije odabran klub za prikaz momčadi");
        }

        String finalOdabraniKlub = odabraniKlub;
        Klub klub = klubovi.stream()
                .filter(k -> k.getNazivKluba().equals(finalOdabraniKlub))
                .findFirst().orElse(null);

        List<Igrac> filteredList = new ArrayList<>(igraci);

        filteredList = filteredList.stream()
                .filter(igrac ->
                        igrac.getKlubId().equals(klub.getId()))
                .collect(Collectors.toList());

        igracTableView.setItems(FXCollections.observableList(filteredList));
    }

    @FXML
    protected void onGenerateButtonClick() throws BazaPodatakaException{
        String odabraniKlub = null;

        try{
            odabraniKlub = klubComboBox.getValue().toString();
        } catch(NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Generiranje postave nije uspjelo");
            alert.setHeaderText("Odaberite klub!");
            alert.showAndWait();

            logger.error("Nije odabran klub za generiranje početnog sastava", ex);

            throw new NullPointerException("Nije odabran klub za generiranje početnog sastava");
        }

        String finalOdabraniKlub = odabraniKlub;

        Klub klub = klubovi.stream()
                .filter(k -> k.getNazivKluba().equals(finalOdabraniKlub))
                .findFirst().orElse(null);

        List<Igrac> filteredList = new ArrayList<>(igraci);

        filteredList = filteredList.stream()
                .filter(igrac ->
                        igrac.getKlubId().equals(klub.getId()))
                .collect(Collectors.toList());

        List<Igrac> igraciPozicije;
        String pocetniSastav = "";

        if(klub.getTrener().getOmiljenaFormacija().equals("4-3-3")){
            //golman
            igraciPozicije = filteredList.stream()
                    .filter(igrac ->
                            igrac.getPozicija().equals("GK"))
                    .collect(Collectors.toList());
            Collections.shuffle(igraciPozicije);
            pocetniSastav = pocetniSastav + "GK: " + igraciPozicije.stream().limit(1).toList().get(0) + "\n\n";

            //lijevi bek
            igraciPozicije = filteredList.stream()
                    .filter(igrac ->
                            igrac.getPozicija().equals("LB"))
                    .collect(Collectors.toList());
            Collections.shuffle(igraciPozicije);
            pocetniSastav = pocetniSastav + "LB: " + igraciPozicije.stream().limit(1).toList().get(0) + "\n";
            //braniči
            igraciPozicije = filteredList.stream()
                    .filter(igrac ->
                            igrac.getPozicija().equals("CB"))
                    .collect(Collectors.toList());
            Collections.shuffle(igraciPozicije);
            pocetniSastav = pocetniSastav + "CB: " + igraciPozicije.stream().limit(2).toList().get(0) + ", "
                    + igraciPozicije.stream().limit(2).toList().get(1) + "\n";
            //desni bek
            igraciPozicije = filteredList.stream()
                    .filter(igrac ->
                            igrac.getPozicija().equals("RB"))
                    .collect(Collectors.toList());
            Collections.shuffle(igraciPozicije);
            pocetniSastav = pocetniSastav + "RB: " + igraciPozicije.stream().limit(1).toList().get(0) + "\n\n";

            //zadnji vezni
            igraciPozicije = filteredList.stream()
                    .filter(igrac ->
                            igrac.getPozicija().equals("CDM"))
                    .collect(Collectors.toList());
            Collections.shuffle(igraciPozicije);
            pocetniSastav = pocetniSastav + "CDM: " + igraciPozicije.stream().limit(1).toList().get(0) + "\n";
            //srednji vezni
            igraciPozicije = filteredList.stream()
                    .filter(igrac ->
                            igrac.getPozicija().equals("CM") ||
                            igrac.getPozicija().equals("CAM"))
                    .collect(Collectors.toList());
            Collections.shuffle(igraciPozicije);
            pocetniSastav = pocetniSastav + "CM: " + igraciPozicije.stream().limit(2).toList().get(0) + ", "
                    + igraciPozicije.stream().limit(2).toList().get(1) + "\n\n";

            //lijevo krilo
            igraciPozicije = filteredList.stream()
                    .filter(igrac ->
                            igrac.getPozicija().equals("LW"))
                    .collect(Collectors.toList());
            Collections.shuffle(igraciPozicije);
            pocetniSastav = pocetniSastav + "LW: " + igraciPozicije.stream().limit(1).toList().get(0) + "\n";
            //špica
            igraciPozicije = filteredList.stream()
                    .filter(igrac ->
                            igrac.getPozicija().equals("ST"))
                    .collect(Collectors.toList());
            Collections.shuffle(igraciPozicije);
            pocetniSastav = pocetniSastav + "ST: " + igraciPozicije.stream().limit(1).toList().get(0) + "\n";
            //desno krilo
            igraciPozicije = filteredList.stream()
                    .filter(igrac ->
                            igrac.getPozicija().equals("RW"))
                    .collect(Collectors.toList());
            Collections.shuffle(igraciPozicije);
            pocetniSastav = pocetniSastav + "RW: " + igraciPozicije.stream().limit(1).toList().get(0);
        } else if(klub.getTrener().getOmiljenaFormacija().equals("4-2-3-1")){
            //golman
            igraciPozicije = filteredList.stream()
                    .filter(igrac ->
                            igrac.getPozicija().equals("GK"))
                    .collect(Collectors.toList());
            Collections.shuffle(igraciPozicije);
            pocetniSastav = pocetniSastav + "GK: " + igraciPozicije.stream().limit(1).toList().get(0) + "\n\n";

            //lijevi bek
            igraciPozicije = filteredList.stream()
                    .filter(igrac ->
                            igrac.getPozicija().equals("LB"))
                    .collect(Collectors.toList());
            Collections.shuffle(igraciPozicije);
            pocetniSastav = pocetniSastav + "LB: " + igraciPozicije.stream().limit(1).toList().get(0) + "\n";
            //braniči
            igraciPozicije = filteredList.stream()
                    .filter(igrac ->
                            igrac.getPozicija().equals("CB"))
                    .collect(Collectors.toList());
            Collections.shuffle(igraciPozicije);
            pocetniSastav = pocetniSastav + "CB: " + igraciPozicije.stream().limit(2).toList().get(0) + ", "
                    + igraciPozicije.stream().limit(2).toList().get(1) + "\n";
            //desni bek
            igraciPozicije = filteredList.stream()
                    .filter(igrac ->
                            igrac.getPozicija().equals("RB"))
                    .collect(Collectors.toList());
            Collections.shuffle(igraciPozicije);
            pocetniSastav = pocetniSastav + "RB: " + igraciPozicije.stream().limit(1).toList().get(0) + "\n\n";

            //srednji vezni
            igraciPozicije = filteredList.stream()
                    .filter(igrac ->
                            igrac.getPozicija().equals("CDM")||
                            igrac.getPozicija().equals("CM"))
                    .collect(Collectors.toList());
            Collections.shuffle(igraciPozicije);
            pocetniSastav = pocetniSastav + "CM: " + igraciPozicije.stream().limit(2).toList().get(0) + ", "
                    + igraciPozicije.stream().limit(2).toList().get(1) + "\n\n";

            //lijevo krilo
            igraciPozicije = filteredList.stream()
                    .filter(igrac ->
                            igrac.getPozicija().equals("LW"))
                    .collect(Collectors.toList());
            Collections.shuffle(igraciPozicije);
            pocetniSastav = pocetniSastav + "LW: " + igraciPozicije.stream().limit(1).toList().get(0) + "\n";
            //ofenzivni vezni
            igraciPozicije = filteredList.stream()
                    .filter(igrac ->
                            igrac.getPozicija().equals("CAM"))
                    .collect(Collectors.toList());
            Collections.shuffle(igraciPozicije);
            pocetniSastav = pocetniSastav + "CAM: " + igraciPozicije.stream().limit(1).toList().get(0) + "\n";
            //desno krilo
            igraciPozicije = filteredList.stream()
                    .filter(igrac ->
                            igrac.getPozicija().equals("RW"))
                    .collect(Collectors.toList());
            Collections.shuffle(igraciPozicije);
            pocetniSastav = pocetniSastav + "RW: " + igraciPozicije.stream().limit(1).toList().get(0) + "\n\n";

            //špica
            igraciPozicije = filteredList.stream()
                    .filter(igrac ->
                            igrac.getPozicija().equals("ST"))
                    .collect(Collectors.toList());
            Collections.shuffle(igraciPozicije);
            pocetniSastav = pocetniSastav + "ST: " + igraciPozicije.stream().limit(1).toList().get(0) + "\n";
        }


//        Collections.shuffle(filteredList);
//        List<Igrac> pocetniSastav = filteredList.stream().limit(11).toList();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Uspješno generirano");
        alert.setHeaderText("Početni sastav");
        alert.setContentText(pocetniSastav);
        alert.showAndWait();
    }

    @FXML
    protected void onSimulateMatchButtonClick() throws BazaPodatakaException{
        String odabraniKlub = null;
        String utakmica = "";

        try{
            odabraniKlub = klubComboBox.getValue().toString();
        } catch(NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Simuliranje utakmice nije uspjelo");
            alert.setHeaderText("Odaberite klub!");
            alert.showAndWait();

            logger.error("Nije odabran klub za simulaciju utakmice", ex);

            throw new NullPointerException("Nije odabran klub za simulaciju utakmice");
        }

        String finalOdabraniKlub = odabraniKlub;

        Klub domaciKlub = klubovi.stream()
                .filter(k -> k.getNazivKluba().equals(finalOdabraniKlub))
                .findFirst().orElse(null);

        List<Klub> filteredList = new ArrayList<>(klubovi);

        filteredList = filteredList.stream()
                .filter(k ->
                        !k.getId().equals(domaciKlub.getId()))
                .collect(Collectors.toList());

        Collections.shuffle(filteredList);

        Klub gostujuciKlub = filteredList.stream().limit(1).toList().get(0);

        utakmica = utakmica + "Stadion: " + domaciKlub.getStadion().nazivStadiona() + "\n\n";

        int domaciGolovi = (int)(Math.random() * (6 - 0 + 1) + 0);
        int gostujuciGolovi = (int)(Math.random() * (6 - 0 + 1) + 0);

        utakmica = utakmica + domaciKlub.getNazivKluba() + " " + domaciGolovi + " : "
                + gostujuciGolovi + " " + gostujuciKlub.getNazivKluba() + "\n\n";

        List<Igrac> filteredListIgraci = new ArrayList<>(igraci);

        List<Igrac> domaciIgraci = filteredListIgraci.stream()
                .filter(igrac ->
                        igrac.getKlubId().equals(domaciKlub.getId()))
                .collect(Collectors.toList());

        List<Igrac> gostujuciIgraci = filteredListIgraci.stream()
                .filter(igrac ->
                        igrac.getKlubId().equals(gostujuciKlub.getId()))
                .collect(Collectors.toList());

        List<Integer> minuteDomaci = new ArrayList<>();
        List<Integer> minuteGostujuci = new ArrayList<>();

        for(int i = 0; i < domaciGolovi; i++){
            Integer minuta = (int)(Math.random() * (90 - 1 + 1) + 1);

            minuteDomaci.add(minuta);
        }
        minuteDomaci = minuteDomaci.stream().sorted().collect(Collectors.toList());

        for(int i = 0; i < gostujuciGolovi; i++){
            Integer minuta = (int)(Math.random() * (90 - 1 + 1) + 1);

            minuteGostujuci.add(minuta);
        }
        minuteGostujuci = minuteGostujuci.stream().sorted().collect(Collectors.toList());

        for(int i = 0; i < domaciGolovi; i++){
            Collections.shuffle(domaciIgraci);

            String gol = domaciIgraci.stream().limit(1).toList().get(0).getPrezime();

            if(minuteDomaci.get(i) < 10){
                int brojac = 24 - gol.length();

                for(int j = 0; j < brojac; j++){
                    gol = gol + " ";
                }
            } else{
                int brojac = 23 - gol.length();

                for(int j = 0; j < brojac; j++){
                    gol = gol + " ";
                }
            }

            gol = gol + minuteDomaci.get(i) + "\n";

            utakmica = utakmica + gol;
        }

        for(int i = 0; i < gostujuciGolovi; i++){
            Collections.shuffle(gostujuciIgraci);

            String gol = "";

            if(minuteGostujuci.get(i) < 10){
                for(int j = 0; j < 24; j++){
                    gol = gol + " ";
                }
            } else{
                for(int j = 0; j < 23; j++){
                    gol = gol + " ";
                }
            }

            gol = gol + minuteGostujuci.get(i);

            gol = gol + "               " +  gostujuciIgraci.stream().limit(1).toList().get(0).getPrezime() + "\n";

            utakmica = utakmica + gol;
        }

        if(domaciGolovi > gostujuciGolovi){
            BazaPodataka.azurirajLigu(domaciKlub.getId(), 3, 1, domaciGolovi, gostujuciGolovi);
            BazaPodataka.azurirajLigu(gostujuciKlub.getId(), 0, 1, gostujuciGolovi, domaciGolovi);
        } else if(domaciGolovi < gostujuciGolovi){
            BazaPodataka.azurirajLigu(domaciKlub.getId(), 0, 1, domaciGolovi, gostujuciGolovi);
            BazaPodataka.azurirajLigu(gostujuciKlub.getId(), 3, 1, gostujuciGolovi, domaciGolovi);
        } else{
            BazaPodataka.azurirajLigu(domaciKlub.getId(), 1, 1, domaciGolovi, gostujuciGolovi);
            BazaPodataka.azurirajLigu(gostujuciKlub.getId(), 1, 1, gostujuciGolovi, domaciGolovi);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Simulacija utakmice uspješna!");
        alert.setHeaderText("Rezultat");
        alert.setContentText(utakmica);
        alert.showAndWait();
    }

    //user
    @FXML
    protected void onSimulateMatchButtonClickUser() throws BazaPodatakaException{
        String odabraniKlub = null;
        String utakmica = "";

        try{
            odabraniKlub = klubComboBox.getValue().toString();
        } catch(NullPointerException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Simuliranje utakmice nije uspjelo");
            alert.setHeaderText("Odaberite klub!");
            alert.showAndWait();

            logger.error("Nije odabran klub za simulaciju utakmice", ex);

            throw new NullPointerException("Nije odabran klub za simulaciju utakmice");
        }

        String finalOdabraniKlub = odabraniKlub;

        Klub domaciKlub = klubovi.stream()
                .filter(k -> k.getNazivKluba().equals(finalOdabraniKlub))
                .findFirst().orElse(null);

        List<Klub> filteredList = new ArrayList<>(klubovi);

        filteredList = filteredList.stream()
                .filter(k ->
                        !k.getId().equals(domaciKlub.getId()))
                .collect(Collectors.toList());

        Collections.shuffle(filteredList);

        Klub gostujuciKlub = filteredList.stream().limit(1).toList().get(0);

        utakmica = utakmica + "Stadion: " + domaciKlub.getStadion().nazivStadiona() + "\n\n";

        int domaciGolovi = (int)(Math.random() * (6 - 0 + 1) + 0);
        int gostujuciGolovi = (int)(Math.random() * (6 - 0 + 1) + 0);

        utakmica = utakmica + domaciKlub.getNazivKluba() + " " + domaciGolovi + " : "
                + gostujuciGolovi + " " + gostujuciKlub.getNazivKluba() + "\n\n";

        List<Igrac> filteredListIgraci = new ArrayList<>(igraci);

        List<Igrac> domaciIgraci = filteredListIgraci.stream()
                .filter(igrac ->
                        igrac.getKlubId().equals(domaciKlub.getId()))
                .collect(Collectors.toList());

        List<Igrac> gostujuciIgraci = filteredListIgraci.stream()
                .filter(igrac ->
                        igrac.getKlubId().equals(gostujuciKlub.getId()))
                .collect(Collectors.toList());

        List<Integer> minuteDomaci = new ArrayList<>();
        List<Integer> minuteGostujuci = new ArrayList<>();

        for(int i = 0; i < domaciGolovi; i++){
            Integer minuta = (int)(Math.random() * (90 - 1 + 1) + 1);

            minuteDomaci.add(minuta);
        }
        minuteDomaci = minuteDomaci.stream().sorted().collect(Collectors.toList());

        for(int i = 0; i < gostujuciGolovi; i++){
            Integer minuta = (int)(Math.random() * (90 - 1 + 1) + 1);

            minuteGostujuci.add(minuta);
        }
        minuteGostujuci = minuteGostujuci.stream().sorted().collect(Collectors.toList());

        for(int i = 0; i < domaciGolovi; i++){
            Collections.shuffle(domaciIgraci);

            String gol = domaciIgraci.stream().limit(1).toList().get(0).getPrezime();

            if(minuteDomaci.get(i) < 10){
                int brojac = 24 - gol.length();

                for(int j = 0; j < brojac; j++){
                    gol = gol + " ";
                }
            } else{
                int brojac = 23 - gol.length();

                for(int j = 0; j < brojac; j++){
                    gol = gol + " ";
                }
            }

            gol = gol + minuteDomaci.get(i) + "\n";

            utakmica = utakmica + gol;
        }

        for(int i = 0; i < gostujuciGolovi; i++){
            Collections.shuffle(gostujuciIgraci);

            String gol = "";

            if(minuteGostujuci.get(i) < 10){
                for(int j = 0; j < 24; j++){
                    gol = gol + " ";
                }
            } else{
                for(int j = 0; j < 23; j++){
                    gol = gol + " ";
                }
            }

            gol = gol + minuteGostujuci.get(i);

            gol = gol + "               " +  gostujuciIgraci.stream().limit(1).toList().get(0).getPrezime() + "\n";

            utakmica = utakmica + gol;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Simulacija utakmice uspješna!");
        alert.setHeaderText("Rezultat");
        alert.setContentText(utakmica);
        alert.showAndWait();
    }
}
