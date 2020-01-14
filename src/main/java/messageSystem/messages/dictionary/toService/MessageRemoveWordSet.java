package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toServlet.MessageToRemoveWordSetServlet;
import services.db.DictionaryService;

public class MessageRemoveWordSet extends MessageToDictionaryService {
    private int userId;
    private int wordSetId;
    private String sessionId;
    public MessageRemoveWordSet(Address from, Address to, int userId, int wordSetId, String sessionId) {
        super(from, to);
        this.userId = userId;
        this.wordSetId = wordSetId;
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(DictionaryService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToRemoveWordSetServlet(getTo(), getFrom(),
                service.removeWordSet(userId, wordSetId)), sessionId);
    }
}
