package com.pogonyi.controller;

import com.pogonyi.db.H2DB;
import com.pogonyi.model.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ScoreViewController extends PreviousPage implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreViewController.class);

    @FXML
    private Button backButton;
    @FXML
    private TableView<Player> table;
    @FXML
    private TableColumn<Player, String> nameCol;
    @FXML
    private TableColumn<Player, Integer> scoreCol;

    private ObservableList<Player> list = FXCollections.observableArrayList();
    private H2DB h2DB;

    @Override
    public void back(ActionEvent actionEvent) {
        super.back(actionEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        h2DB = new H2DB();
        nameCol.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<Player, Integer>("score"));
        List<Player> topTen = h2DB.getTopTen();
        list.addAll(topTen);
        table.setItems(list);
    }
}
