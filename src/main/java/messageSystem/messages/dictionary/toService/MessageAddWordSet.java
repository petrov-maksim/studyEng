package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toServlet.MessageToAddWordSetServlet;
import services.db.DictionaryService;

public class MessageAddWordSet extends MessageToDictionaryService {
    private String name;
    private String sessionId;
    private int userId;
    public MessageAddWordSet(Address from, Address to, String name, String sessionId, int userId) {
        super(from, to);
        this.name = name;
        this.sessionId = sessionId;
        this.userId = userId;
    }

    @Override
    protected void exec(DictionaryService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToAddWordSetServlet(getTo(),getFrom(),
                service.addWordSetForUser(name, userId)), sessionId);
    }
}
