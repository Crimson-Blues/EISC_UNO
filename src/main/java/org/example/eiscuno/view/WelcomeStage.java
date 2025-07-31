package org.example.eiscuno.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.eiscuno.controller.WelcomeStageController;

import java.io.IOException;

public class WelcomeStage extends Stage{

    WelcomeStageController welcomeStageController;

    public WelcomeStage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/eiscuno/welcome-stage.fxml"));
        Parent root;
        try{
            root = fxmlLoader.load();
        }catch(IOException e){
            throw new IOException("Error loading FXML file", e);
        }
        welcomeStageController = fxmlLoader.getController();
        Scene scene = new Scene(root);
        setTitle("Welcome");

        getIcons().add(
                new Image(String.valueOf(getClass().getResource("/org/example/eiscuno/favicon.png"))));
        setScene(scene);
        setResizable(false);
        show();
    }

    public WelcomeStageController getWelcomeStageController() {
        return this.welcomeStageController;
    }


    /**
     * Closes the instance of WelcomeStage.
     * This method is used to clean up resources when the game stage is no longer needed.
     */
    public static void deleteInstance() {
        WelcomeStageHolder.INSTANCE.close();
        WelcomeStageHolder.INSTANCE = null;
    }

    /**
     * Retrieves the singleton instance of WelcomeStage.
     *
     * @return the singleton instance of WelcomeStage.
     * @throws IOException if an error occurs while creating the instance.
     */
    public static WelcomeStage getInstance() throws IOException {
        return WelcomeStageHolder.INSTANCE != null ?
                WelcomeStageHolder.INSTANCE :
                (WelcomeStageHolder.INSTANCE = new WelcomeStage());
    }

    /**
     * Holder class for the singleton instance of WelcomeStage.
     * This class ensures lazy initialization of the singleton instance.
     */
    private static class WelcomeStageHolder {
        private static WelcomeStage INSTANCE;
    }

}
