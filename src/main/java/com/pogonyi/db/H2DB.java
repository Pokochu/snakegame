package com.pogonyi.db;

import com.pogonyi.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class H2DB {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2DB.class);

    Connection connection;
    Statement statement;

    public H2DB() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:~/snake", "", "");
            statement = connection.createStatement();
        } catch (Exception e) {
            LOGGER.error("Cannot create connection to DB: {}", e.getMessage());
            System.exit(0);
        }
    }

    public void createDB() {
        try {
            statement.execute("DROP TABLE IF EXISTS PLAYERS;");
            statement.execute("CREATE TABLE PLAYERS (\n" +
                    "    score INT NOT NULL,\n" +
                    "    name VARCHAR(250) NOT NULL PRIMARY KEY\n" +
                    ")");
        } catch (SQLException e) {
            LOGGER.info("Cannot create database: {}", e.getMessage());
        }
        LOGGER.info("Database successfully initialized...");
    }

    public void insertData(Player player) {
        try {
            StringBuilder builder = new StringBuilder("INSERT INTO PLAYERS VALUES(");
            builder.append(player.getScore())
                    .append(", '")
                    .append(player.getName())
                    .append("')");
            statement.execute(builder.toString());
        } catch (SQLException e) {
            LOGGER.info("Insertion to database failed: {}", e.getMessage());
        }
        LOGGER.info("Inserted {} with {} pts to db...", player.getName(), player.getScore());
    }

    public List<Player> getTopTen() {
        List<Player> result = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PLAYERS ORDER BY score DESC LIMIT 10");
            while (resultSet.next()) {
                result.add(new Player(resultSet.getInt(1), resultSet.getString(2)));
            }
        } catch (SQLException e) {
            LOGGER.info("Cannot query from database: {}", e.getMessage());
            return null;
        }
        return result;
    }

    public int getTopScore() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM PLAYERS ORDER BY score DESC LIMIT 1");
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.info("Cannot query database: {}", e.getMessage());
        }
        return 0;
    }
}
