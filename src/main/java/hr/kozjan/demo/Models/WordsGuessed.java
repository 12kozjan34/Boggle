package hr.kozjan.demo.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;

public class WordsGuessed implements Serializable {
    private transient StringProperty word;
    private transient IntegerProperty points;

    public WordsGuessed(String word, Integer points) {
        this.word = new SimpleStringProperty(word);
        this.points = new SimpleIntegerProperty(points);
    }

    public String getWord() {
        return word.get();
    }

    public StringProperty wordProperty() {
        return word;
    }

    public void setWord(String word) {
        this.word.set(word);
    }

    public int getPoints() {
        return points.get();
    }

    public IntegerProperty pointsProperty() {
        return points;
    }

    public void setPoints(int points) {
        this.points.set(points);
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(word.get());
        out.writeObject(points.get());
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        String wordValue = (String) in.readObject();
        int pointsValue = (int) in.readObject();
        this.word = new SimpleStringProperty(wordValue);
        this.points = new SimpleIntegerProperty(pointsValue);
    }
}
