package com.example.footballmanager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passPasswordField;

    HashMap<String, String> mapaUsera;

    public void initialize(){
        mapaUsera = new HashMap<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("dat/prijava.txt")))){
            List<String> datotekaString = bufferedReader.lines().collect(Collectors.toList());
            for(int i = 0; i <= datotekaString.size() / 2; i += 2){
                String key, value;
                key = datotekaString.get(i);
                value = datotekaString.get(i + 1);
                mapaUsera.put(key, value);
            }
        }
        catch(IOException ex){
            logger.error("Greška prilikom učitavanja datoteke za prijavu");
        }

        usernameTextField.setPromptText("Username");
        passPasswordField.setPromptText("Password");
    }

    public void prikaziUserEkran() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menu-bar-user.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Football manager");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }
    public void prikaziAdminEkran() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("menu-bar.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        HelloApplication.getMainStage().setTitle("Football manager");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void login() throws NoSuchAlgorithmException {
        Encryption encryption = new Encryption();
        String username = usernameTextField.getText();
        String password = encryption.encryptString(passPasswordField.getText());
        if(provjeriUsera(username, password, mapaUsera)){
            if(username.compareTo("admin") == 0){
                try {
                    prikaziAdminEkran();
                } catch (IOException e) {
                    logger.error("Nemoguće otvoriti admin ekran!");
                    throw new RuntimeException(e);
                }
            }
            if(username.compareTo("user") == 0){
                try {
                    prikaziUserEkran();
                } catch (IOException e) {
                    logger.error("Nemoguće otvoriti user ekran!");
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public boolean provjeriUsera(String username, String password, HashMap<String, String> mapaUserPassword){
        StringBuilder poruka = new StringBuilder();

        if(username.isEmpty()){
            poruka.append("Unesite username!");
        }

        if(password.isEmpty()){
            poruka.append("Unesite lozinku!");
        }

        if(username.isEmpty() || password.isEmpty() ){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Greška!");
            alert.setHeaderText("Login failed");
            alert.setContentText(poruka.toString());

            alert.showAndWait();
        }

        if(!username.isEmpty() && !password.isEmpty()){
            if(mapaUserPassword.containsKey(username)){
                if(password.compareTo(mapaUserPassword.get(username)) == 0){
                    return true;
                }
            }
        }

        if(!mapaUserPassword.containsKey(username) && !username.isEmpty()){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Greška!");
            alert.setHeaderText("Login failed");
            alert.setContentText("Krivi username!");

            alert.showAndWait();
        }

        if(mapaUserPassword.containsKey(username)){
            if(password.compareTo(mapaUserPassword.get(username)) != 0 && !password.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Greška!");
                alert.setHeaderText("Login failed");
                alert.setContentText("Kriva lozinka!");

                alert.showAndWait();
            }}

        return false;
    }

}
