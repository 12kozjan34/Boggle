package hr.kozjan.demo.Utils;

import hr.kozjan.demo.Models.Position;
import hr.kozjan.demo.Models.WordsGuessed;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class GameUtils {

    public static void paintLetter(Label clickedLetter, int clickCounter) {
        if (clickCounter > 1) {
            clickedLetter.getStyleClass().remove("clicked");
            clickCounter = 0;
        } else {
            clickedLetter.getStyleClass().add("clicked");
            clickCounter = 0;
        }
    }

    public static String getWord(ArrayList<String> word) {
        StringBuilder sb = new StringBuilder();
        for (String character : word) {
            sb.append(character);
        }
        return sb.toString();
    }

    public static List<Label> getLabelPosition(Label clickedLetter, int NUM_ROWS, int NUM_COL, Label[][] gameBoard) {
        List<Label> fields = new ArrayList<>();
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COL; j++) {
                if (gameBoard[i][j] == clickedLetter) {
                    Position pos = new Position(i, j);
                    fields = getAvailableFields(pos, NUM_ROWS, NUM_COL, gameBoard);
                }
            }
        }
        return fields;
    }

    public static List<Label> getAvailableFields(Position position, int NUM_ROWS, int NUM_COL, Label[][] gameBoard) {
        List<Label> fields = new ArrayList<>();

        int[][] directions = {
                {-1, 0},
                {0, 1},
                {1, 0},
                {0, -1},
                {-1, -1},
                {-1, 1},
                {1, -1},
                {1, 1},
                {0, 0}
        };

        for (int[] dir : directions) {
            int newRow = position.getX() + dir[0];
            int newCol = position.getY() + dir[1];

            if (newRow >= 0 && newRow < NUM_ROWS && newCol >= 0 && newCol < NUM_COL) {
                if (!gameBoard[newRow][newCol].getStyleClass().contains("clicked")) {
                    fields.add(gameBoard[newRow][newCol]);
                }
                if (newRow == position.getX() && newCol == position.getY()) {
                    fields.add(gameBoard[newRow][newCol]);
                }
            }
        }
        return fields;
    }

    public static void fillTable(ObservableList<WordsGuessed> tableData, TableView<WordsGuessed> tvWords, Label lblPoints) {
        int points = 0;
        tvWords.setItems(tableData);
        for (WordsGuessed list : tableData) {
            int point = list.getPoints();
            points += point;
            lblPoints.setText(String.valueOf(points));
        }
    }

    public static void setNewBoard(int NUM_ROWS, int NUM_COL, Label[][] gameBoard, List<Label> fields, ArrayList<String> word, int clickCounter) {
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COL; j++) {
                gameBoard[i][j].getStyleClass().remove("clicked");
            }
        }
        fields.clear();
        clickCounter = 0;
        word.clear();
    }

    private static Timeline timer;
    private static int remainingTime;

    public static void startTimer(Label lblTimer, ObservableList<WordsGuessed> obsWords, Label lblPoints) {
        remainingTime = 180;
        if (timer == null) {
            Timeline finalTimer = timer;
            timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                if (remainingTime > 0) {
                    remainingTime--;
                    lblTimer.setText(Integer.toString(remainingTime));
                } else {
                    stopTimer(finalTimer);
                    Platform.runLater(() -> {
                        MessageUtils.showDialog(Alert.AlertType.INFORMATION, "Game over", "Game is over " + "you have earned " + lblPoints.getText() + " points");
                    });
                    obsWords.clear();
                    startTimer(lblTimer, obsWords, lblPoints);
                }
            }));
            timer.setCycleCount(Timeline.INDEFINITE);
            timer.play();
        }
    }

    public static void stopTimer(Timeline timer) {
        if (timer != null) {
            timer.stop();
        }
    }
}
