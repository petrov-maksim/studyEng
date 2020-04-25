package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import services.db.DictionaryService;

/**
 * Сообщение направленное на удаление слов
 */
public class MsgRemoveWordsForUser extends MsgToDictionaryService {
    private Integer wordIds[];
    private int userId;

    public MsgRemoveWordsForUser(Address from, Address to, Integer[] wordIds, int userId) {
        super(from, to);
        this.wordIds = wordIds;
        this.userId = userId;

    }

    @Override
    protected void exec(DictionaryService service) {
        service.removeWordsForUser(wordIds, userId);
    }
}
