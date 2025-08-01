package org.example.eiscuno;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.eiscuno.view.WelcomeStage;

import java.io.IOException;

/**
 * The main class of the EISC Uno application.
 * @author Juan Diego Cardenas 2416437
 * @author Juan David Guar 2341909
 * @author Juan Pablo Piedrahita 2342374
 * @author Juan Esteban Arias Saldana 2417915
 * @version 1.0
 */
public class Main extends Application {

    /**
     * The main method of the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the application.
     *
     * @param primaryStage the primary stage of the application
     * @throws IOException if an error occurs while loading the stage
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        //GameUnoStage.getInstance();
        WelcomeStage.getInstance();
    }
}