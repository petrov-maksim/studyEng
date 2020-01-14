package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toServlet.MessageToGetTranslationsForWordServlet;
import services.db.DictionaryService;

public class MessageGetTranslationsForWord extends MessageToDictionaryService {
    private final int wordId;
    private final String sessionId;
    public MessageGetTranslationsForWord(Address from, Address to, int wordId, String sessionId) {
        super(from, to);
        this.wordId = wordId;
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(DictionaryService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToGetTranslationsForWordServlet(getTo(), getFrom(),
                service.getTranslationsForWord(wordId)),sessionId);
    }
}
