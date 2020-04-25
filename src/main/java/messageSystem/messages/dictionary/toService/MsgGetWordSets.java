package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toServlet.MsgToGetWordSetsServlet;
import services.db.DictionaryService;

/**
 * Сообщение направленное на получение наборов
 */
public class MsgGetWordSets extends MsgToDictionaryService {
    private int userId;
    private String sessionId;
    public MsgGetWordSets(Address from, Address to, int userId, String sessionId) {
        super(from, to);
        this.userId = userId;
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(DictionaryService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MsgToGetWordSetsServlet(getTo(), getFrom(),
                service.getWordSetsForUser(userId)), sessionId);
    }
}
