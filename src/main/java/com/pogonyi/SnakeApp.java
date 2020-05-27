package com.pogonyi;

import com.pogonyi.controller.SettingsViewController;
import com.pogonyi.db.H2DB;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.prefs.Preferences;

import static com.pogonyi.Constants.DOWN;
import static com.pogonyi.Constants.LEFT;
import static com.pogonyi.Constants.RIGHT;
import static com.pogonyi.Constants.UP;

public class SnakeApp extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnakeApp.class);

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    private Preferences preferences;
    private H2DB h2DB;

    @Override
    public void start(Stage primaryStage) throws Exception {
        h2DB = new H2DB();
        h2DB.createDB();
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass()
                    .getResource("/views/MainView.fxml")));

        } catch (Exception ex) {
            LOGGER.error("Error when loading MainView.fxml file. {}", ex.getMessage());
            System.exit(0);
        }
        try {
            primaryStage.getIcons()
                    .add(new Image(Objects.requireNonNull(getClass()
                            .getResourceAsStream("/modelImages/neti.png"))));
        } catch (Exception ex) {
            LOGGER.error("images/neti.png file not found.");
            System.exit(0);
        }
        primaryStage.setTitle("Snake");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();

        setDefCont();

        primaryStage.show();
    }
    private void setDefCont() {
        LOGGER.info("set controls");
        preferences = Preferences.userRoot().node(SettingsViewController.class.getName());

        preferences.put(UP, UP);
        preferences.put(DOWN, DOWN);
        preferences.put(RIGHT, RIGHT);
        preferences.put(LEFT, LEFT);
    }
}
