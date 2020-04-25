package messageSystem.messages.dictionary.toService;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import services.db.DictionaryService;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Базовый класс сообщения в DictionaryService
 */
public abstract class MsgToDictionaryService extends Message {
    public MsgToDictionaryService(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof DictionaryService)
            exec((DictionaryService) abonent);
        else
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Wrong abonent");
    }

    protected abstract void exec(DictionaryService service);
}
