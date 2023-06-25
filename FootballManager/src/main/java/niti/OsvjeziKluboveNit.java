package niti;

import baza.BazaPodataka;
import entitet.Klub;
import iznimke.BazaPodatakaException;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;

public class OsvjeziKluboveNit implements Runnable{

    private TableView<Klub> list;

    public OsvjeziKluboveNit(TableView<Klub> list) {
        this.list = list;
    }

    @Override
    public void run() {
        try {
            list.setItems(FXCollections.observableList(BazaPodataka.dohvatiKlubove()));
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
    }
}
