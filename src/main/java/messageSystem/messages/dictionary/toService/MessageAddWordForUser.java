package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toServlet.MessageToAddWordForUserServlet;
import services.db.DictionaryService;

public class MessageAddWordForUser extends MessageToDictionaryService {
    private String word;
    private String translation;
    private int userId;
    private int wordSetId;
    private String sessionId;

    public MessageAddWordForUser(Address from, Address to, String word, String translation, int userId, String sessionId) {
        super(from, to);
        this.word = word;
        this.translation = translation;
        this.userId = userId;
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(DictionaryService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToAddWordForUserServlet(getTo(), getFrom(),
                service.addWordForUser(word, translation, userId)),sessionId);
    }
}
