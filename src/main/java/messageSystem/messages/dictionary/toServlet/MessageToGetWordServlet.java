package messageSystem.messages.dictionary.toServlet;

import entities.Word;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.dictionary.user.GetWordForUserServlet;

public class MessageToGetWordServlet extends Message {
    private final Word[] words;
    public MessageToGetWordServlet(Address from, Address to, Word[] words) {
        super(from, to);
        this.words = words;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GetWordForUserServlet)
            exec((GetWordForUserServlet) abonent);
    }

    private void exec(GetWordForUserServlet servlet){
        servlet.handle(words);
    }
}
