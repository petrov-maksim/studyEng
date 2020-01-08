package messageSystem.messages.content;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.content.ContentServlet;

public class MessageToContentServlet extends Message {
    private String videos[];
    private String texts[];
    public MessageToContentServlet(Address from, Address to, String[] videos, String[] texts) {
        super(from, to);
        this.videos = videos;
        this.texts = texts;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof ContentServlet)
            exec((ContentServlet) abonent);
    }

    private void exec(ContentServlet servlet){
        servlet.handleRequest(videos, texts);
    }
}
