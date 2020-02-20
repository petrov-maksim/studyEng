package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toServlet.MessageToGetWordsForUserServlet;
import services.db.DictionaryService;

public class MessageGetWordsForUser extends MessageToDictionaryService {
    private final String sessionId;
    private final int userId;
    private final int index;
    private final int num;

    public MessageGetWordsForUser(Address from, Address to, String sessionId, int userId, int index, int num) {
        super(from, to);
        this.sessionId = sessionId;
        this.userId = userId;
        this.index = index;
        this.num = num;
    }

    @Override
    protected void exec(DictionaryService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToGetWordsForUserServlet(getTo(), getFrom(),
                service.getNWordsForUser(userId, num, index)), sessionId);
    }
}
