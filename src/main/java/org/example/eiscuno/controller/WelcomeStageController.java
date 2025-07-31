package org.example.eiscuno.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.example.eiscuno.model.planeTextFiles.PlaneTextFileHandler;
import org.example.eiscuno.view.GameUnoStage;
import org.example.eiscuno.view.WelcomeStage;

import java.io.IOException;
import java.util.Stack;

public class WelcomeStageController {

    @FXML
    private TextField usernameField;

    private String nickName;
    private PlaneTextFileHandler planeTextFileHandler;
    private Boolean isContinue;

    @FXML
    public void initialize() {
        nickName = "";
        planeTextFileHandler = new PlaneTextFileHandler();
    }

    @FXML
    public void onHandlePlayButton() throws IOException {
        if(!usernameField.getText().isEmpty()) {
            nickName = usernameField.getText();
            planeTextFileHandler.write("PlayerData.csv", nickName);
            isContinue = false;
            GameUnoStage.getInstance();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "¡Enter a username before continuing!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("/org/example/eiscuno/styles.css").toExternalForm());
            dialogPane.getStyleClass().add("warning-label");
            alert.setTitle("Warning");
            alert.showAndWait();
        }
    }

    @FXML
    public void onHandleContinueButton() throws IOException {
        isContinue = true;
        GameUnoStage.getInstance();
    }

    @FXML
    public void onHandleQuitButton(){
        WelcomeStage.deleteInstance();
        System.exit(0);
    }

    @FXML
    public void onHandleCreditsButton(){

    }

    public Boolean returnIsContinue(){
        return this.isContinue;
    }
}
