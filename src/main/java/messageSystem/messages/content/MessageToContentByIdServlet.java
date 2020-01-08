package messageSystem.messages.content;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.content.ContentByIdServlet;

public class MessageToContentByIdServlet extends Message {
    private final String payload;
    public MessageToContentByIdServlet(Address from, Address to, String payload) {
        super(from, to);
        this.payload = payload;
    }

    @Override
    public void exec(Abonent abonent) {
        if(abonent instanceof ContentByIdServlet)
            exec((ContentByIdServlet) abonent);
    }

    private void exec(ContentByIdServlet servlet){
        servlet.handleRequest(payload);
    }
}
