package messageSystem.messages.dictionary.toService;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import services.db.DictionaryService;

public abstract class MessageToDictionaryService extends Message {
    public MessageToDictionaryService(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof DictionaryService)
            exec((DictionaryService) abonent);
        else
            System.out.println("Wrong abonent in MessageToDictionaryService");
    }

    protected abstract void exec(DictionaryService service);
}
