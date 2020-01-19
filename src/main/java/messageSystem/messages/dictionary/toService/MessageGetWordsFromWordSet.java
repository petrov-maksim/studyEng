package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toServlet.MessageToGetWordsFromWordSet;
import services.db.DictionaryService;

public class MessageGetWordsFromWordSet extends MessageToDictionaryService {
    private final int userId;
    private final int wordSetId;
    private final int index;
    private final int num;
    private final String sessionId;

    public MessageGetWordsFromWordSet(Address from, Address to, int userId, int wordSetId, int index, int num, String sessionId) {
        super(from, to);
        this.userId = userId;
        this.wordSetId = wordSetId;
        this.index = index;
        this.num = num;
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(DictionaryService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToGetWordsFromWordSet(getTo(), getFrom(),
                service.getNWordsFromWordSet(userId, wordSetId, num, index)), sessionId);
    }
}
