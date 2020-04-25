package messageSystem.messages.dictionary.toServlet;

import entities.Word;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.dictionary.wordSet.GetWordsFromWordSetServlet;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сообщение со словами из набора
 */
public class MsgToGetWordsFromWordSet extends Message {
    private final Collection<Word> words;
    public MsgToGetWordsFromWordSet(Address from, Address to, Collection<Word> words) {
        super(from, to);
        this.words = words;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GetWordsFromWordSetServlet)
            exec((GetWordsFromWordSetServlet) abonent);
        else
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Wrong abonent");
    }

    private void exec(GetWordsFromWordSetServlet servlet){
        servlet.handle(words);
    }
}
