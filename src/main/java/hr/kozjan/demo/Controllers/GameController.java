package hr.kozjan.demo.Controllers;

import hr.kozjan.demo.Models.WordsGuessed;
import hr.kozjan.demo.Utils.GameUtils;
import hr.kozjan.demo.Utils.MessageUtils;
import hr.kozjan.demo.Utils.WebUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameController {
    public TableView<WordsGuessed> tvWords;
    public TableColumn<WordsGuessed, String> tcWord, tcPoints;
    private ObservableList<WordsGuessed> obsWords;
    private static final Integer NUM_ROWS = 4;
    private static final Integer NUM_COL = 4;
    @FXML
    private Label one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, thirteen, fourteen, fifteen, sixteen, lblTimer, gameMessage, lblPoints;
    private Label lastSource = null;
    private List<Label> fields;
    private ArrayList<String> word;
    public static Label[][] gameBoard;
    private List<Label> clickedLabels;
    private int clickCounter = 0;
    private MenuItem miNewGame;

    public void initialize() {
        gameBoard = new Label[NUM_ROWS][NUM_COL];

        clickedLabels = new ArrayList<>();

        lblPoints.setText("0");

        gameBoard[0][0] = one;
        gameBoard[0][1] = two;
        gameBoard[0][2] = three;
        gameBoard[0][3] = four;
        gameBoard[1][0] = five;
        gameBoard[1][1] = six;
        gameBoard[1][2] = seven;
        gameBoard[1][3] = eight;
        gameBoard[2][0] = nine;
        gameBoard[2][1] = ten;
        gameBoard[2][2] = eleven;
        gameBoard[2][3] = twelve;
        gameBoard[3][0] = thirteen;
        gameBoard[3][1] = fourteen;
        gameBoard[3][2] = fifteen;
        gameBoard[3][3] = sixteen;

        Random random = new Random();

        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COL; j++) {
                int letterNum = random.nextInt(26) + 65;
                char letter = (char) letterNum;
                gameBoard[i][j].setText(Character.toString(letter));
                gameBoard[i][j].getStyleClass().remove("clicked");
            }
        }

        fields = new ArrayList<>();

        word = new ArrayList<>();

        gameMessage.setText("");

        tcWord.setCellValueFactory(new PropertyValueFactory<>("Word"));
        tcPoints.setCellValueFactory(new PropertyValueFactory<>("Points"));

        obsWords = FXCollections.observableArrayList();
        obsWords.clear();
        tvWords.setItems(obsWords);

        GameUtils.startTimer(lblTimer,obsWords, lblPoints);
    }

    public void letterClicked(Event event) {
        clickCounter = 0;
        gameMessage.setText("");
        Label clickedLetter = (Label) event.getSource();

        if (clickedLetter != lastSource) {
            clickCounter = 0;
        }

        if (lastSource == null) {
            clickCounter++;
            GameUtils.paintLetter(clickedLetter, clickCounter);
            GameUtils.getLabelPosition(clickedLetter, NUM_ROWS, NUM_COL, gameBoard);

            lastSource = clickedLetter;

            assert clickedLetter != null;
            if (clickedLetter.getStyleClass().contains("clicked")) {
                word.add(clickedLetter.getText());
                clickedLabels.add(clickedLetter);
            } else {
                word.remove(clickedLetter.getText());
                clickedLabels.remove(clickedLetter);
            }

        } else {
            clickCounter++;
            if (clickedLetter.getStyleClass().contains("clicked")) {
                clickCounter++;
            }
            fields = GameUtils.getLabelPosition(lastSource, NUM_ROWS, NUM_COL, gameBoard);
            boolean foundField = false;

            for (Label field : fields) {
                if (field.equals(clickedLetter)) {
                    GameUtils.paintLetter(clickedLetter, clickCounter);
                    foundField = true;

                    if (clickedLetter.getStyleClass().contains("clicked")) {
                        word.add(clickedLetter.getText());
                        clickedLabels.add(clickedLetter);
                        lastSource = clickedLetter;
                    } else {
                        word.remove(clickedLetter.getText());
                        clickedLabels.remove(clickedLetter);
                        if (clickedLabels.isEmpty()) {
                            lastSource = null;
                            return;
                        }
                        lastSource = clickedLabels.getLast();
                    }

                    if (word.size() >= 3) {
                        int points = 0;
                        boolean test = false;
                        String wordAttempt = GameUtils.getWord(word);

                        if (!obsWords.isEmpty()) {
                            for (WordsGuessed obsWord : obsWords) {
                                if (obsWord.getWord().equals(wordAttempt)) {
                                    return;
                                } else {
                                    test = WebUtils.checkForWordTruth(wordAttempt);
                                }
                            }
                        } else {
                            test = WebUtils.checkForWordTruth(wordAttempt);
                        }

                        if (test) {
                            if (wordAttempt.length() == 3) {
                                points = 100;
                            } else if (wordAttempt.length() == 4) {
                                points = 200;
                            } else if (wordAttempt.length() >= 5) {
                                points = 500;
                            }
                            WordsGuessed guessed = new WordsGuessed(wordAttempt, points);
                            obsWords.add(guessed);
                            GameUtils.fillTable(obsWords, tvWords, lblPoints);
                            GameUtils.setNewBoard(NUM_ROWS, NUM_COL, gameBoard, fields, word, clickCounter);
                            lastSource = null;
                            clickedLabels.clear();
                            clickCounter = 0;
                            break;
                        }
                    }
                    break;
                }
            }
            if (!foundField) {
                gameMessage.setText("You can't click this filed");
            }
        }
    }

    public void newGameClicked() {
        initialize();
    }
}