package messageSystem.messages.grammar.toService;

import messageSystem.Address;
import services.db.GrammarService;

/**
 * Сообщение направленное на изменение текущего уровня
 */
public class MsgUpdateCurrentLvl extends MsgToGrammarService {
    private final int userId;
    private final String lvl;

    public MsgUpdateCurrentLvl(Address from, Address to,int userId, String lvl) {
        super(from, to);
        this.userId = userId;
        this.lvl = lvl;
    }

    @Override
    public void exec(GrammarService service) {
        service.updateCurrentLvl(userId, lvl);
    }
}
