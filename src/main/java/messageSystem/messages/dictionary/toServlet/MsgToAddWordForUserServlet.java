package messageSystem.messages.dictionary.toServlet;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.dictionary.user.AddWordForUserServlet;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сообщение с идентификатором нового слова
 */
public class MsgToAddWordForUserServlet extends Message {
    private int wordId;

    public MsgToAddWordForUserServlet(Address from, Address to, int wordId) {
        super(from, to);
        this.wordId = wordId;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof AddWordForUserServlet)
            exec((AddWordForUserServlet) abonent);
        else
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Wrong abonent");
    }

    private void exec(AddWordForUserServlet servlet){
        servlet.handle(wordId);
    }
}
