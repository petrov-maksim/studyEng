package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toServlet.MessageToUpdateWordSetServlet;
import services.db.DictionaryService;

public class MessageUpdateWordSet extends MessageToDictionaryService {
    private String newName;
    private String sessionId;
    private int userId;
    private int wordSetId;

    public MessageUpdateWordSet(Address from, Address to, String newName, String sessionId, int userId, int wordSetId) {
        super(from, to);
        this.newName = newName;
        this.sessionId = sessionId;
        this.userId = userId;
        this.wordSetId = wordSetId;
    }

    @Override
    protected void exec(DictionaryService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToUpdateWordSetServlet(getTo(), getFrom(),
                service.updateWordSetName(newName, userId, wordSetId)), sessionId);
    }
}
