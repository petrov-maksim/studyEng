package messageSystem.messages.trainings.toServlet;

import entities.Word;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.trainings.GetUnlearnedWordsServlet;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сообщение с неизученными словами
 */
public class MsgToGetUnlearnedWordsServlet extends Message {
    private final Collection<Word> words;

    public MsgToGetUnlearnedWordsServlet(Address from, Address to, Collection<Word> words) {
        super(from, to);
        this.words = words;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GetUnlearnedWordsServlet)
            exec((GetUnlearnedWordsServlet) abonent);
        else
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Wrong abonent");
    }

    private void exec(GetUnlearnedWordsServlet servlet){
        servlet.handle(words);
    }
}