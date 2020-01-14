package messageSystem.messages.dictionary.toServlet;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.dictionary.wordSet.RemoveWordSetServlet;

public class MessageToRemoveWordSetServlet extends Message {
    private boolean status;
    public MessageToRemoveWordSetServlet(Address from, Address to, boolean status) {
        super(from, to);
        this.status = status;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof RemoveWordSetServlet)
            exec((RemoveWordSetServlet) abonent);
    }

    private void exec(RemoveWordSetServlet servlet){
        servlet.handle(status);
    }
}
