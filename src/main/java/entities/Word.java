package entities;

public class Word {
    private String word;
    private String translations[];
    private String example;
    private int frequency;
    private int id;

    public String getWord() {
        return word;
    }

    public String[] getTranslations() {
        return translations;
    }

    public String getExample() {
        return example;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getId() {
        return id;
    }
}
