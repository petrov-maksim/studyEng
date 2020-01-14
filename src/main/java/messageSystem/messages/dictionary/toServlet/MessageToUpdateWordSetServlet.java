package messageSystem.messages.dictionary.toServlet;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.dictionary.wordSet.UpdateWordSetServlet;

public class MessageToUpdateWordSetServlet extends Message {
    private boolean status;
    public MessageToUpdateWordSetServlet(Address from, Address to, boolean status) {
        super(from, to);
        this.status = status;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof UpdateWordSetServlet)
            exec((UpdateWordSetServlet) abonent);
    }

    private void exec(UpdateWordSetServlet servlet){
        servlet.handle(status);
    }
}
