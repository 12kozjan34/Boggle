package hr.kozjan.demo.Models;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameModel implements Serializable {
    private LabelInfo[][] gameBoard;
    private List<WordsGuessed> obsWords;
    private int remainingTime;
    private List<LabelInfo> clickedLetters;
    private int points;

    public GameModel(Label[][] labels, List<WordsGuessed> obsWords, int remainingTime, List<Label> letters, int points) {
        this.obsWords = obsWords;
        this.remainingTime = remainingTime;
        this.points = points;

        int numRows = labels.length;
        int numCols = labels[0].length;

        gameBoard = new LabelInfo[numRows][numCols];
        clickedLetters = new ArrayList<>();

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                gameBoard[i][j] = new LabelInfo(labels[i][j].getText(), labels[i][j].getId());
            }
        }

        for (Label label : letters) {
            clickedLetters.add(new LabelInfo(label.getText(), label.getId()));
        }
    }

    public List<Label> toLabelList(List<LabelInfo> clickedLetters) {
        List<Label> labels = new ArrayList<>();

        for (LabelInfo labelText : clickedLetters) {
            Label label = new Label(labelText.getText());
            label.setId(labelText.getId());
            labels.add(label);
        }

        return labels;
    }

    public Label[][] toLabelArray(LabelInfo[][] gameBoard) {
        int numRows = gameBoard.length;
        int numCols = gameBoard[0].length;

        Label[][] labels = new Label[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                labels[i][j] = new Label(gameBoard[i][j].getText());
                labels[i][j].setId(gameBoard[i][j].getId());
            }
        }

        return labels;
    }

    public Label[][] getGameBoard() {
        Label[][] converted;
        converted = toLabelArray(gameBoard);
        return converted;
    }

    public List<WordsGuessed> getObsWords() {
        return obsWords;
    }

    public void setObsWords(List<WordsGuessed> obsWords) {
        this.obsWords = obsWords;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public List<Label> getClickedLetters() {
        return toLabelList(clickedLetters);
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(gameBoard);
        out.writeObject(clickedLetters);
        out.writeObject(obsWords);
        out.writeObject(remainingTime);
        out.writeObject(points);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        gameBoard = (LabelInfo[][]) in.readObject();
        clickedLetters = (List<LabelInfo>) in.readObject();
        obsWords = (List<WordsGuessed>)in.readObject();
        remainingTime = (int)in.readObject();
        points = (int)in.readObject();
    }
}
