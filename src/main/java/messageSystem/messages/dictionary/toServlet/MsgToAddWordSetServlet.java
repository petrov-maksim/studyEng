package messageSystem.messages.dictionary.toServlet;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.dictionary.wordSet.AddWordSetServlet;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сообщение с идентификатором нового набора слов
 */
public class MsgToAddWordSetServlet extends Message {
    private final int wordSetId;

    public MsgToAddWordSetServlet(Address from, Address to, int wordSetId) {
        super(from, to);
        this.wordSetId = wordSetId;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof AddWordSetServlet)
            exec((AddWordSetServlet) abonent);
        else
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Wrong abonent");
    }

    private void exec(AddWordSetServlet servlet){
        servlet.handle(wordSetId);
    }
}
