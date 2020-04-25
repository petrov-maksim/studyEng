package messageSystem.messages.grammar.toService;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import services.db.GrammarService;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Базовый класс сообщения в GrammarService
 */
public abstract class MsgToGrammarService extends Message {
    public MsgToGrammarService(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GrammarService)
            exec((GrammarService) abonent);
        else
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Wrong abonent");;
    }

    public abstract void exec(GrammarService service);
}
