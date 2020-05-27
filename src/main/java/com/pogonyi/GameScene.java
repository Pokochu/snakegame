package com.pogonyi;

import com.pogonyi.controller.SaveScoreViewController;
import com.pogonyi.controller.SettingsViewController;
import com.pogonyi.db.H2DB;
import com.pogonyi.model.Frog;
import com.pogonyi.model.Snake;
import com.sun.javafx.scene.traversal.Direction;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.prefs.Preferences;

import static com.pogonyi.Constants.*;

public class GameScene extends Scene {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameScene.class);

    private int width = 1000;
    private int height = 700;

    private GraphicsContext graphicsContext;
    private Canvas canvas;
    private Frog frog;
    private Snake snake;
    private long time;
    private Label pauseLabel;
    private Label gameOverLabel;
    private Label scoreLabel;
    private Label inGameScoreLabel;
    private Label lifeLabel;
    private Label topScoreLabel;
    private Preferences preferences;
    private SnakeTimer timer;
    private ESCHandler escHandler;
    private DirectionKeyHandler keyHandler;
    private H2DB h2DB;

    private int score = 0;
    private int lives =5;
    private int foodPoint = 100;
    private boolean inGame = false;
    private boolean paused = false;
    private boolean gameOver = false;

    public GameScene(Parent root, long time) {
        this(root);
        this.time = time;
    }

    public GameScene(Parent root) {
        super(root);

        h2DB = new H2DB();
        preferences = Preferences.userRoot().node(SettingsViewController.class.getName());
        canvas = createGameCanvas();
        ((Pane) root).getChildren().add(canvas);
        graphicsContext = canvas.getGraphicsContext2D();
        frog = new Frog(PIXELSIZE, PIXELSIZE);
        timer = new SnakeTimer();
        escHandler = new ESCHandler();
        keyHandler = new  DirectionKeyHandler();
        addEventHandler(KeyEvent.KEY_PRESSED, keyHandler);
        addEventHandler(KeyEvent.KEY_PRESSED, escHandler);

        initLabels();
        initScreen();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Snake getSnake() {
        return snake;
    }

    private void initLabels() {
        pauseLabel = initLabel("Paused", width /2f - 25, height /2f);
        gameOverLabel = initLabel("Game Over!", width /2f - 75, height /2f - 40);
        scoreLabel = initLabel("", width /2f - 115, height /2f - 10);
        inGameScoreLabel = initInGameLabels("inGameScoreLabel", -0, 0);
        lifeLabel = initInGameLabels("lifeLabel", 0, 20);
        topScoreLabel = initInGameLabels("topScoreLabel", 0, 40);
    }

    private Canvas createGameCanvas() {
        int newWidth = StringUtils.isEmpty(preferences.get("WIDTH", "")) ? width : Integer.parseInt(preferences.get("WIDTH", ""));
        int newHeight = StringUtils.isEmpty(preferences.get("HEIGHT", "")) ? height : Integer.parseInt(preferences.get("HEIGHT", ""));
        checkScreenBounds(newWidth, newHeight);
        width = newWidth;
        height = newHeight;
        return new Canvas(width, height);
    }

    private void checkScreenBounds(int newWidth, int newHeight) {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        if(newWidth > screenBounds.getWidth() || newHeight > screenBounds.getHeight()) {
            LOGGER.error("Max screen resolution can be: {}x{}, but {}:{} was set in the Settings.", screenBounds.getWidth(),
                    screenBounds.getHeight(), newWidth, newHeight);
            System.exit(0);
        }
    }

    private Label initLabel(String name, double x, double y) {
        Label label = new Label(name);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/GeneralStyle.css")).toString());
        return label;
    }

    private Label initInGameLabels(String id, double x, double y) {
        Label label = new Label();
        label.setId(id);
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/GeneralStyle.css")).toString());
        ((AnchorPane) getRoot()).getChildren().add(label);
        return label;
    }

    private void initScreen() {
        inGameScoreLabel.setText("Score: " + score + "pt.");
        lifeLabel.setText("Lives: " + lives);
        topScoreLabel.setText("Top score: " + h2DB.getTopScore());
        renderBackground();
        createSnake();
        frog.setRandomPosition(width, height);
        renderGameElements();
    }

    private void renderBackground() {
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillRect(0, 0, width, height);
    }

    private void createSnake() {
        snake = new Snake(new Point2D(width / 2f, height / 2f),
                new Point2D(width / 2f - PIXELSIZE, height / 2f), PIXELSIZE);
    }

    private void renderGameElements() {
        snake.render(graphicsContext);
        frog.render(graphicsContext);
        snake.render(graphicsContext);
    }

    private boolean checkSnake() {
        double posX = snake.getHead().getPosition().getX();
        double posY = snake.getHead().getPosition().getY();
        return posX >= width || posX < 0 || posY >= height || posY < 0;
    }

    private void renderGameOverMsg() {
        scoreLabel.setText("Your score: " + score);

        Button saveYourScoreBtn = initButton("Save Score", width /2f - 85, height /2f + 20);
        Button restartBtn = initButton("Restart, ", width /2f - 125, height /2f + 50);
        Button exitBtn = initButton("EXIT", width /2f - 50, height /2f + 100);
        Button backBtn = initButton("Back", width /2f + 30, height /2f + 50);

        exitBtn.setOnMouseClicked(e -> System.exit(0));
        saveYourScoreBtn.setOnMouseClicked(event -> {
            Stage stage = (Stage) saveYourScoreBtn.getScene().getWindow();
            Parent root = null;
            try {
                SaveScoreViewController controller = new SaveScoreViewController();
                controller.setScore(String.valueOf(score));
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().
                        getResource("/views/SaveScoreView.fxml")));
                loader.setController(controller);
                root = loader.load();
            } catch (IOException e1) {
                LOGGER.error("views/WelcomeView.fxml file not found {}", e1.getMessage());
                System.exit(0);
            }
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        });
        restartBtn.setOnMouseClicked(e -> {
            gameOver = false;
            ((AnchorPane) getRoot()).getChildren().removeAll(gameOverLabel, scoreLabel, saveYourScoreBtn, restartBtn, exitBtn, backBtn);

            frog.setRandomPosition(width, height);
            addEventHandler(KeyEvent.KEY_PRESSED, keyHandler);
            resetCounters();
            initScreen();
        });
        backBtn.setOnMouseClicked(e -> {
            Stage stage = (Stage) getWindow();
            Parent root = null;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().
                        getResource("/views/MainView.fxml")));
            } catch (IOException e1) {
                LOGGER.error("views/WelcomeView.fxml file not found");
                System.exit(0);
            }
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        });

        ((AnchorPane) getRoot()).getChildren().addAll(gameOverLabel, scoreLabel, saveYourScoreBtn, restartBtn, exitBtn, backBtn);
    }

    private void resetCounters() {
        score = 0;
        lives = 5;
    }

    private Button initButton(String name, double x, double y) {
        Button button = new Button(name);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/GameOverStyle.css")).toString());
        return button;
    }

    private class SnakeTimer extends AnimationTimer {
        private long lastUpdate = 0;

        @Override
        public void start() {
            super.start();
            inGame = true;
        }

        @Override
        public void handle(long now) {
            // if the game isn't paused it will refresh the screen in every 100 milliseconds
            if (now - lastUpdate >= time) {
                addEventHandler(KeyEvent.KEY_PRESSED, keyHandler);
                lastUpdate = now;

                snake.move();
                if (snake.getHead().intersect(frog)) {
                    do {
                        frog.setRandomPosition(width, height);
                    } while (snake.intersect(frog));
                    score += foodPoint;
                    snake.grow();

                    inGameScoreLabel.setText("Score: " + score + "pt.");
                    renderBackground();
                }

                renderGameElements();
                if (snake.collide() || checkSnake()) {
                    lives--;
                    if(lives == 0) {
                        gameOver = true;
                    }
                    this.stop();
                    initScreen();
                }

                if (gameOver) {
                    // stop the timer
                    this.stop();
                    renderGameOverMsg();
                }
            }
        }
    }

    private class ESCHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
            KeyCode kc = event.getCode();
            if (kc == KeyCode.ESCAPE && inGame) {
                if (paused) {
                    timer.start();
                    ((AnchorPane) getRoot()).getChildren().remove(pauseLabel);
                } else {
                    timer.stop();
                    ((AnchorPane) getRoot()).getChildren().add(pauseLabel);
                }
                paused = !paused;
            }
        }
    }

    private class DirectionKeyHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
            KeyCode kc = event.getCode();

            String key = kc.toString();
            if ((key.equals(preferences.get(RIGHT, ""))
                    || key.equals(preferences.get(LEFT, ""))
                    || key.equals(preferences.get(UP, ""))
                    || key.equals(preferences.get(DOWN, "")))
                    && !gameOver) {
                timer.start();
                paused = false;
            }
            if (key.equals(preferences.get(RIGHT, "")) && snake.getDirection() != Direction.LEFT) {
                snake.setDirection(Direction.RIGHT);
            } else {
                if (key.equals(preferences.get(LEFT, "")) && snake.getDirection() != Direction.RIGHT) {
                    snake.setDirection(Direction.LEFT);
                } else {
                    if (key.equals(preferences.get(DOWN, "")) && snake.getDirection() != Direction.UP) {
                        snake.setDirection(Direction.DOWN);
                    } else {
                        if (key.equals(preferences.get(UP, "")) && snake.getDirection() != Direction.DOWN) {
                            snake.setDirection(Direction.UP);
                        }
                    }
                }
            }

            removeEventHandler(KeyEvent.KEY_PRESSED, this);
        }
    }
}
