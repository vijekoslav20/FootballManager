package com.example.footballmanager;

import baza.BazaPodataka;
import entitet.Igrac;
import iznimke.BazaPodatakaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IgracPretragaController {

    List<Igrac> igraci = BazaPodataka.dohvatiIgrace();

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

    public IgracPretragaController() throws BazaPodatakaException {
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
    protected void onSearchButtonClick() {
        String unesenoIme = imeIgrac.getText();
        String unesenoPrezime = prezimeIgrac.getText();
        String uneseniDatum = Optional.ofNullable(datumRodenjaIgrac.getValue())
                .map(d -> d.format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))).orElse("");
        String unesenaVrijednost = vrijednostIgrac.getText();
        String unesenaPozicija = pozicijaIgrac.getValue();
        String unesenoDrzavljanstvo = drzavljanstvoIgrac.getText();

        List<Igrac> filteredList = new ArrayList<>(igraci);

        if(pozicijaIgrac.getValue() == null) {
            filteredList = filteredList.stream()
                    .filter(igrac ->
                            igrac.getIme().toLowerCase().contains(unesenoIme) &&
                                    igrac.getPrezime().toLowerCase().contains(unesenoPrezime) &&
                                    igrac.getDatumRodenja()
                                            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))
                                            .contains(uneseniDatum) &&
                                    igrac.getDrzavljanstvo().toLowerCase().contains(unesenoDrzavljanstvo))
                    .filter(igrac -> igrac.getVrijednost() >= Integer.parseInt(unesenaVrijednost))
                    .collect(Collectors.toList());
        } else{
            filteredList = filteredList.stream()
                    .filter(igrac ->
                            igrac.getIme().toLowerCase().contains(unesenoIme) &&
                                    igrac.getPrezime().toLowerCase().contains(unesenoPrezime) &&
                                    igrac.getDatumRodenja()
                                            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy."))
                                            .contains(uneseniDatum) &&
                                    igrac.getVrijednost().toString().contains(unesenaVrijednost) &&
                                    igrac.getPozicija().contains(unesenaPozicija) &&
                                    igrac.getDrzavljanstvo().toLowerCase().contains(unesenoDrzavljanstvo))
                    .collect(Collectors.toList());
        }

        igracTableView.setItems(FXCollections.observableList(filteredList));
    }
}
