package com.pogonyi.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.prefs.Preferences;

import static com.pogonyi.Constants.*;

public class SettingsViewController extends PreviousPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(SettingsViewController.class);

    @FXML
    private Button backBtn;
    @FXML
    private TextField upText;
    @FXML
    private TextField downText;
    @FXML
    private TextField rightText;
    @FXML
    private TextField leftText;
    @FXML
    private TextField widthText;
    @FXML
    private TextField heightText;


    private Preferences preferences;

    public SettingsViewController() {
        preferences = Preferences.userRoot().node(SettingsViewController.class.getName());
    }

    @FXML
    public void initialize() {
        setControlText();
    }

    public void resetControls() {
        setDefaultControl();
        setControlText();
    }

    @Override
    public void back(ActionEvent actionEvent) {
        super.back(actionEvent);
        saveNewScreenResolution();
    }

    public void changeText(KeyEvent keyEvent) {
        Node textField = (Node) keyEvent.getSource();
        ((TextField) textField).setText(keyEvent.getCode().toString());

        if (textField.getId().equals(upText.getId())) {
            LOGGER.info("up stored " + keyEvent.getCode());
            preferences.put(UP, keyEvent.getCode().toString());
        } else {
            if (textField.getId().equals(downText.getId())) {
                LOGGER.info("down stored " + keyEvent.getCode());
                preferences.put(DOWN, keyEvent.getCode().toString());
            } else {
                if (textField.getId().equals(rightText.getId())) {
                    LOGGER.info("right stored " + keyEvent.getCode());
                    preferences.put(RIGHT, keyEvent.getCode().toString());
                } else {
                    if (textField.getId().equals(leftText.getId())) {
                        LOGGER.info("left stored: " + keyEvent.getCode());
                        preferences.put(LEFT, keyEvent.getCode().toString());
                    }
                }
            }
        }
    }

    private void saveNewScreenResolution() {
        String width = widthText.getText();
        String height = heightText.getText();
        preferences.put("WIDTH", width);
        preferences.put("HEIGHT", height);
        LOGGER.info("Screen resolution was set to {}x{}...", width, height);
    }

    private void setDefaultControl() {
        preferences.put(UP, UP);
        preferences.put(DOWN, DOWN);
        preferences.put(RIGHT, RIGHT);
        preferences.put(LEFT, LEFT);
    }

    private void setControlText() {
        upText.setText(preferences.get(UP, ""));
        downText.setText(preferences.get(DOWN, ""));
        rightText.setText(preferences.get(RIGHT, ""));
        leftText.setText(preferences.get(LEFT, ""));
    }
}
