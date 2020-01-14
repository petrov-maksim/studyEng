package messageSystem.messages.dictionary.toServlet;

import entities.WordSet;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.dictionary.wordSet.GetWordSetsServlet;

public class MessageToGetWordSetsServlet extends Message {
    private WordSet wordSets [];
    public MessageToGetWordSetsServlet(Address from, Address to, WordSet[] wordSets) {
        super(from, to);
        this.wordSets = wordSets;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GetWordSetsServlet)
            exec((GetWordSetsServlet) abonent);
    }

    private void exec(GetWordSetsServlet servlet){
        servlet.handle(wordSets);
    }
}
