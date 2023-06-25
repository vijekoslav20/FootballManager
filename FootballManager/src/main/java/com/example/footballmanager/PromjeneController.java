package com.example.footballmanager;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import niti.AzuriranjeNit;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PromjeneController {
    @FXML
    private TextArea textArea;

    @FXML
    synchronized void initialize(){
        AzuriranjeNit azuriranjeNit = new AzuriranjeNit(textArea);
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(azuriranjeNit);
        executorService.shutdown();
    }
}