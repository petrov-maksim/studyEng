package entities.content;

public class Video extends Content {
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Video{" +
                "link='" + link + '\'' +
                '}';
    }
}
