package messageSystem.messages.grammar.toServlet;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.grammar.GetCurrentLvlServlet;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сообщение с текущим уровнем
 */
public class MsgToGetCurrentLvlServlet extends Message {
    private final String lvl;

    public MsgToGetCurrentLvlServlet(Address from, Address to, String lvl) {
        super(from, to);
        this.lvl = lvl;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GetCurrentLvlServlet)
            exec((GetCurrentLvlServlet) abonent);
        else
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Wrong abonent");
    }

    private void exec(GetCurrentLvlServlet servlet){
        servlet.handle(lvl);
    }
}
