package com.pogonyi.controller;

import com.pogonyi.db.H2DB;
import com.pogonyi.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class SaveScoreViewController extends PreviousPage implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaveScoreViewController.class);

    @FXML
    private Button backBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private TextField nameText;
    @FXML
    private Label scoreText;

    private String score;
    private H2DB h2DB;

    public SaveScoreViewController() {
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void saveScore(ActionEvent event) {
        h2DB.insertData(new Player(Integer.parseInt(score), nameText.getText()));
        super.back(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        h2DB = new H2DB();
        scoreText.setText(score);
    }
}
