package messageSystem.messages.dictionary.toServlet;

import entities.Word;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.dictionary.GetWordsFromWordSetServlet;

import java.util.Collection;

public class MessageToGetWordsFromWordSet extends Message {
    private final Collection<Word> words;
    public MessageToGetWordsFromWordSet(Address from, Address to, Collection<Word> words) {
        super(from, to);
        this.words = words;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GetWordsFromWordSetServlet)
            exec((GetWordsFromWordSetServlet) abonent);
    }

    private void exec(GetWordsFromWordSetServlet servlet){
        servlet.handle(words);
    }
}
