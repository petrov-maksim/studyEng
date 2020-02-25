package entities;

public class Training {
    private int id;
    private String name;
    private int numOfUnlearnedWords;

    public Training(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfUnlearnedWords() {
        return numOfUnlearnedWords;
    }

    public void setNumOfUnlearnedWords(int numOfUnlearnedWords) {
        this.numOfUnlearnedWords = numOfUnlearnedWords;
    }

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numOfUnlearnedWords=" + numOfUnlearnedWords +
                '}';
    }
}
