package com.pogonyi.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public abstract class PreviousPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreviousPage.class);

    public static final String FILE_NOT_FOUND_STOPPING_APPLICATION = "MainView fxml file not found, stopping application...";

    public void back(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();

        Stage stage = (Stage) button.getScene().getWindow();
        Parent root = null;

        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("views/MainView.fxml")));
        } catch (Exception ex) {
            LOGGER.error(FILE_NOT_FOUND_STOPPING_APPLICATION);
            System.exit(0);
        }

        stage.setScene(new Scene(root));
        stage.show();
    }
}
