package messageSystem.messages.grammar.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.grammar.toServlet.MsgToGetCurrentLvlServlet;
import services.db.GrammarService;

/**
 * Сообщение направленное на получение текущего уровня
 */
public class MsgGetCurrentLvl extends MsgToGrammarService {
    private final int userId;
    private final String sessionId;

    public MsgGetCurrentLvl(Address from, Address to, int userId, String sessionId) {
        super(from, to);
        this.userId = userId;
        this.sessionId = sessionId;
    }

    @Override
    public void exec(GrammarService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MsgToGetCurrentLvlServlet(getTo(), getFrom(),
                service.getCurrentLvl(userId)), sessionId);
    }
}
