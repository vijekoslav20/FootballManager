package niti;

import baza.BazaPodataka;
import entitet.Stadion;
import iznimke.BazaPodatakaException;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;

public class OsvjeziStadioneNit implements Runnable{

    private TableView<Stadion> list;

    public OsvjeziStadioneNit(TableView<Stadion> list) {
        this.list = list;
    }

    @Override
    public void run() {
        try {
            list.setItems(FXCollections.observableList(BazaPodataka.dohvatiStadione()));
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
    }
}
