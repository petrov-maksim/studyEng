package messageSystem.messages.dictionary;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;

public abstract class MessageToDictionaryService extends Message {
    public MessageToDictionaryService(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {

    }
}
