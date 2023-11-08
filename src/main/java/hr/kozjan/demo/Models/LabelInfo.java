package hr.kozjan.demo.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class LabelInfo implements Serializable {
    private String text;
    private String id;

    public LabelInfo(String text, String id) {
        this.text = text;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        // Implement custom serialization logic here if needed
        out.writeObject(text);
        out.writeObject(id);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Implement custom deserialization logic here if needed
        text = (String) in.readObject();
        id = (String) in.readObject();
    }
}
