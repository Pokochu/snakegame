package com.pogonyi.controller;

import com.pogonyi.GameScene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class MainViewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainViewController.class);

    @FXML
    private Button startBtn;
    @FXML
    private Button settingsBtn;
    @FXML
    private Button scoreBtn;

    private Parent root;
    private Stage parentView;

    private long difficulty = 100_000_000;

    public void exit() {
        System.exit(0);
    }

    public void startBtnAction() {
        AnchorPane root = new AnchorPane();

        parentView = (Stage) startBtn.getScene().getWindow();
        parentView.setScene(new GameScene(root, difficulty));
        parentView.centerOnScreen();
        parentView.show();
    }

    public void settingsBtnAction() {

        parentView = (Stage) settingsBtn.getScene().getWindow();

        setView(parentView, "/views/SettingsView.fxml");
    }

    public void scoreBtnAction() {
        parentView = (Stage) scoreBtn.getScene().getWindow();

        setView(parentView, "/views/ScoreView.fxml");
    }

    private void setView(Stage stage, String location) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(location)));
        } catch (Exception ex) {
            LOGGER.error("{} file not found {}", location, ex.getMessage());
            ex.printStackTrace();
            System.exit(0);
        }

        stage.setScene(new Scene(root));
        stage.show();
    }
}
