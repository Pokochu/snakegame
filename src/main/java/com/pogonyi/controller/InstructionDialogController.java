package com.pogonyi.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class InstructionDialogController implements Initializable {

    @FXML
    private Label rightLabel;
    @FXML
    private Label leftLabel;
    @FXML
    private Label upLabel;
    @FXML
    private Label downLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Preferences prefs = Preferences.userRoot().node(SettingsViewController.class.getName());
        rightLabel.setText(prefs.get("RIGHT", ""));
        leftLabel.setText(prefs.get("LEFT", ""));
        upLabel.setText(prefs.get("UP", ""));
        downLabel.setText(prefs.get("DOWN", ""));
    }
}
