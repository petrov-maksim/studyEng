package messageSystem.messages.content.toServlet;

import entities.content.Video;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.content.GetAllContentVideosServlet;

import java.util.List;

public class MessageToGetAllContentVideosServlet extends Message {
    private final List<Video> videos;
    public MessageToGetAllContentVideosServlet(Address from, Address to, List<Video> videos) {
        super(from, to);
        this.videos = videos;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GetAllContentVideosServlet)
            exec((GetAllContentVideosServlet) abonent);
    }

    private void exec(GetAllContentVideosServlet servlet){
        servlet.handle(videos);
    }
}
