package messageSystem.messages.dictionary.toServlet;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.dictionary.wordSet.AddWordSetServlet;

public class MessageToAddWordSetServlet extends Message {
    private final int wordSetId;
    public MessageToAddWordSetServlet(Address from, Address to, int wordSetId) {
        super(from, to);
        this.wordSetId = wordSetId;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof AddWordSetServlet)
            exec((AddWordSetServlet) abonent);
    }

    private void exec(AddWordSetServlet servlet){
        servlet.handle(wordSetId);
    }
}
