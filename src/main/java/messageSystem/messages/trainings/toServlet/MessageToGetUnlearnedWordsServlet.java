package messageSystem.messages.trainings.toServlet;

import entities.Word;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.trainings.GetUnlearnedWordsServlet;

import java.util.Collection;

public class MessageToGetUnlearnedWordsServlet extends Message {
    private final Collection<Word> words;
    public MessageToGetUnlearnedWordsServlet(Address from, Address to, Collection<Word> words) {
        super(from, to);
        this.words = words;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GetUnlearnedWordsServlet)
            exec((GetUnlearnedWordsServlet) abonent);
    }

    private void exec(GetUnlearnedWordsServlet servlet){
        servlet.handle(words);
    }
}