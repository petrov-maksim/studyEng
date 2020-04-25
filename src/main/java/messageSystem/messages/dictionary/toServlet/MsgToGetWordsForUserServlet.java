package messageSystem.messages.dictionary.toServlet;

import entities.Word;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.dictionary.user.GetWordsForUserServlet;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сообщение со словами
 */
public class MsgToGetWordsForUserServlet extends Message {
    private final Collection<Word> words;

    public MsgToGetWordsForUserServlet(Address from, Address to, Collection<Word> words) {
        super(from, to);
        this.words = words;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GetWordsForUserServlet)
            exec((GetWordsForUserServlet) abonent);
        else
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Wrong abonent");
    }

    private void exec(GetWordsForUserServlet servlet){
        servlet.handle(words);
    }
}
