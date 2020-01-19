package messageSystem.messages.dictionary.toServlet;

import entities.Word;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.dictionary.user.GetWordsForUserServlet;

import java.util.Collection;

public class MessageToGetWordServlet extends Message {
    private final Collection<Word> words;
    public MessageToGetWordServlet(Address from, Address to, Collection<Word> words) {
        super(from, to);
        this.words = words;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GetWordsForUserServlet)
            exec((GetWordsForUserServlet) abonent);
    }

    private void exec(GetWordsForUserServlet servlet){
        servlet.handle(words);
    }
}
