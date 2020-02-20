package entities;

import java.util.ArrayList;
import java.util.List;

public class Word {
    private String word;
    private List<String> translations = new ArrayList<>();
    private String example;
    private int id;
    private int index;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getTranslations() {
        return translations;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", translations=" + translations +
                ", example='" + example + '\'' +
                ", id=" + id +
                '}';
    }
}
