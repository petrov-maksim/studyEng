package messageSystem.messages.dictionary.toServlet;

import entities.WordSet;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.dictionary.wordSet.GetWordSetsServlet;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сообщение с наборами слов
 */
public class MsgToGetWordSetsServlet extends Message {
    private List<WordSet> wordSets;

    public MsgToGetWordSetsServlet(Address from, Address to, List<WordSet> wordSets) {
        super(from, to);
        this.wordSets = wordSets;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GetWordSetsServlet)
            exec((GetWordSetsServlet) abonent);
        else
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Wrong abonent");
    }

    private void exec(GetWordSetsServlet servlet){
        servlet.handle(wordSets);
    }
}
