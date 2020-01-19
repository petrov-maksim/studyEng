package entities;

public class WordSet {
    private int wordSetId;
    private String name;
    private int size;

    public int getWordSetId() {
        return wordSetId;
    }

    public void setWordSetId(int wordSetId) {
        this.wordSetId = wordSetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "WordSet{" +
                "wordSetId=" + wordSetId +
                ", name='" + name + '\'' +
                ", size=" + size +
                '}';
    }
}
