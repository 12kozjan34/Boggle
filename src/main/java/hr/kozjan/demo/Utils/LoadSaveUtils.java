package hr.kozjan.demo.Utils;

import hr.kozjan.demo.Controllers.GameController;
import hr.kozjan.demo.Models.GameModel;
import hr.kozjan.demo.Models.WordsGuessed;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoadSaveUtils {
    public static void save(int NUM_ROWS, int NUM_COL, Label[][] gameBoard, Label lblTimer, Label lblPoints, ObservableList<WordsGuessed> obsWords, List<Label> clickedLabels) {
        Label[][] gameBoardLetters = new Label[NUM_ROWS][NUM_COL];

        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COL; j++) {
                if (!gameBoard[i][j].getText().isBlank()) {
                    gameBoardLetters[i][j] = gameBoard[i][j];
                }
            }
        }

        int remainingTime = Integer.parseInt(lblTimer.getText());
        int points = Integer.parseInt(lblPoints.getText());

        List<WordsGuessed> guessed = obsWords.stream().toList();

        GameModel gameBoardToSave = new GameModel(gameBoardLetters, guessed, remainingTime, clickedLabels, points);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("saveGame.dat"))) {
            oos.writeObject(gameBoardToSave);

            MessageUtils.showDialog(Alert.AlertType.INFORMATION, "Saved", "Game is successfully saved");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void load(Integer NUM_ROWS, Integer NUM_COL, List<Label> clickedLabels, Label[][] gameBoard, ArrayList<String> word, ObservableList<WordsGuessed> obsWords, TableView<WordsGuessed> tvWords, Label lblPoints, Label lblTimer) {
        GameModel gameModelReceived;

        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream("saveGame.dat"))) {
            gameModelReceived = (GameModel) oos.readObject();

            initializeWithResources(gameModelReceived, NUM_ROWS,NUM_COL,clickedLabels,gameBoard,word,obsWords,tvWords,lblPoints,lblTimer);

            MessageUtils.showDialog(Alert.AlertType.INFORMATION,"Loaded","Game successfully loaded");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void initializeWithResources(GameModel gameModelReceived, Integer NUM_ROWS, Integer NUM_COL, List<Label> clickedLabels, Label[][] gameBoard, ArrayList<String> word, ObservableList<WordsGuessed> obsWords, TableView<WordsGuessed> tvWords, Label lblPoints, Label lblTimer) {
        if (gameModelReceived.getClickedLetters() == null) {
            return;
        } else {
            clickedLabels = gameModelReceived.getClickedLetters();
        }

        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COL; j++) {
                gameBoard[i][j].setText(gameModelReceived.getGameBoard()[i][j].getText());
            }
        }

        Label[][] finalGameBoard = gameBoard;
        clickedLabels.forEach(l -> {
            for (int i = 0; i < NUM_ROWS; i++) {
                for (int j = 0; j < NUM_COL; j++) {
                    Label current = finalGameBoard[i][j];
                    if (l.getId().equals(current.getId())) {
                        current.getStyleClass().add("clicked");
                        word.add(current.getText());
                    }
                }
            }
        });

        int timer = gameModelReceived.getRemainingTime();
        int points = gameModelReceived.getPoints();

        List<WordsGuessed> words = gameModelReceived.getObsWords();

        obsWords.addAll(words);

        GameUtils.fillTable(obsWords, tvWords, lblPoints);

        lblPoints.setText(String.valueOf(points));
        GameUtils.startTimer(lblTimer, obsWords, lblPoints, timer);

    }
}
