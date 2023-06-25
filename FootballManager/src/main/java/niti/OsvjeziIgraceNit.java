package niti;

import baza.BazaPodataka;
import entitet.Igrac;
import iznimke.BazaPodatakaException;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;

public class OsvjeziIgraceNit implements Runnable{

    private TableView<Igrac> list;
    public OsvjeziIgraceNit(TableView<Igrac> list) {
        this.list = list;
    }

    @Override
    public void run() {
        try {
            list.setItems(FXCollections.observableList(BazaPodataka.dohvatiIgrace()));
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
    }
}
