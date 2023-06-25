package niti;

import baza.BazaPodataka;
import entitet.Trener;
import iznimke.BazaPodatakaException;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;

public class OsvjeziTrenereNit implements Runnable{

    private TableView<Trener> list;

    public OsvjeziTrenereNit(TableView<Trener> list) {
        this.list = list;
    }

    @Override
    public void run() {
        try {
            list.setItems(FXCollections.observableList(BazaPodataka.dohvatiTrenere()));
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
    }
}
