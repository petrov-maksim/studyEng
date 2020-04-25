package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toServlet.MsgToAddWordForUserServlet;
import services.db.DictionaryService;

/**
 * Сообщение добавления слова пользователю
 */
public class MsgAddWordForUser extends MsgToDictionaryService {
    private String word;
    private String translation;
    private int userId;
    private String sessionId;

    public MsgAddWordForUser(Address from, Address to, String word, String translation, int userId, String sessionId) {
        super(from, to);
        this.word = word;
        this.translation = translation;
        this.userId = userId;
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(DictionaryService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MsgToAddWordForUserServlet(getTo(), getFrom(),
                service.addWordForUser(word, translation, userId)),sessionId);
    }
}
