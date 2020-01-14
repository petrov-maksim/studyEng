package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toServlet.MessageToAddTranslationServlet;
import services.db.DictionaryService;

public class MessageAddTranslation extends MessageToDictionaryService {
    private String translation;
    private String sessionId;
    private int wordId;
    private int userId;
    public MessageAddTranslation(Address from, Address to, String translation, String sessionId, int wordId, int userId) {
        super(from, to);
        this.translation = translation;
        this.sessionId = sessionId;
        this.wordId = wordId;
        this.userId = userId;
    }

    @Override
    protected void exec(DictionaryService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToAddTranslationServlet(getTo(), getFrom(),
                service.addTranslation(wordId, translation, userId)), sessionId);
    }
}
