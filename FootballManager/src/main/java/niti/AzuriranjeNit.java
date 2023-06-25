package niti;

import entitet.Serijalizacija;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AzuriranjeNit implements Runnable{
    private TextArea textArea;

    public AzuriranjeNit(TextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void run() {
        Serijalizacija serijalizacija = new Serijalizacija();

        List<String> listPromjene = new ArrayList<>();

        try {
            listPromjene.add(serijalizacija.deserijaliziraj("dat//igraciSerijalizacija.bin").toString()+"\n");
            listPromjene.add(serijalizacija.deserijaliziraj("dat//treneriSerijalizacija.bin").toString()+"\n");
            listPromjene.add(serijalizacija.deserijaliziraj("dat//stadioniSerijalizacija.bin").toString()+"\n");
            listPromjene.add(serijalizacija.deserijaliziraj("dat//kluboviSerijalizacija.bin").toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Platform.runLater(()->{
            textArea.setText(listPromjene.toString());
        });
    }
}
