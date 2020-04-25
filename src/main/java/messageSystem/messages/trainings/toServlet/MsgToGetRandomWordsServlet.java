package messageSystem.messages.trainings.toServlet;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.trainings.GetRandomWordsServlet;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сообщение с случайными словами
 */
public class MsgToGetRandomWordsServlet extends Message {
    private final List<String> words;

    public MsgToGetRandomWordsServlet(Address from, Address to, List<String> words) {
        super(from, to);
        this.words = words;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GetRandomWordsServlet)
            exec((GetRandomWordsServlet) abonent);
        else
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Wrong abonent");
    }

    private void exec(GetRandomWordsServlet servlet){
        servlet.handle(words);
    }
}
