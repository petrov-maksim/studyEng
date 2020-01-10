package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toServlet.MessageToRemoveWordServlet;
import services.db.DictionaryService;

public class MessageRemoveWordsForUser extends MessageToDictionaryService {
    private Integer wordIds[];
    private int userId;
    private String sessionId;
    public MessageRemoveWordsForUser(Address from, Address to, Integer[] wordIds, int userId, String sessionId) {
        super(from, to);
        this.wordIds = wordIds;
        this.userId = userId;
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(DictionaryService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToRemoveWordServlet(getTo(), getFrom(),
                service.removeWordForUser(wordIds, userId)), sessionId);
    }
}
