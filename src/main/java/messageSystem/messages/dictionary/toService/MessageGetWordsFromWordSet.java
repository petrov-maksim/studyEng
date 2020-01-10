package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toServlet.MessageToGetWordServlet;
import services.db.DictionaryService;

public class MessageGetWordsFromWordSet extends MessageToDictionaryService {
    private final int wordSetId;
    private final int index;
    private final int num;
    private final String sessionId;

    public MessageGetWordsFromWordSet(Address from, Address to, int wordSetId, int index, int num, String sessionId) {
        super(from, to);
        this.wordSetId = wordSetId;
        this.index = index;
        this.num = num;
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(DictionaryService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToGetWordServlet(getTo(), getFrom(),
                service.getNWordsFromWordSet(wordSetId, num, index)), sessionId);
    }
}
