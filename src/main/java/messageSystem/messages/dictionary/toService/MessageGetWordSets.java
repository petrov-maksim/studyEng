package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toServlet.MessageToGetWordSetsServlet;
import services.db.DictionaryService;

public class MessageGetWordSets extends MessageToDictionaryService {
    private int userId;
    private String sessionId;
    public MessageGetWordSets(Address from, Address to, int userId, String sessionId) {
        super(from, to);
        this.userId = userId;
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(DictionaryService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToGetWordSetsServlet(getTo(), getFrom(),
                service.getWordSetsForUser(userId)), sessionId);
    }
}
