package messageSystem.messages.grammar.toService;

import entities.Word;
import messageSystem.Address;
import services.db.GrammarService;

/**
 * Сообщение направленное на добавление слов пользователю
 */
public class MsgAddWordsForUser extends MsgToGrammarService {
    private final Word words [];
    private final int userId;

    public MsgAddWordsForUser(Address from, Address to,int userId, Word[] words) {
        super(from, to);
        this.words = words;
        this.userId = userId;
    }

    @Override
    public void exec(GrammarService service) {
        service.addWordsForUser(userId, words);
    }
}
