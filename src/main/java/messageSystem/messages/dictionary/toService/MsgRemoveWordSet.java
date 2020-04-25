package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import services.db.DictionaryService;

/**
 * Сообщение направленное на удаление набора слов
 */
public class MsgRemoveWordSet extends MsgToDictionaryService {
    private int wordSetId;
    public MsgRemoveWordSet(Address from, Address to, int wordSetId) {
        super(from, to);
        this.wordSetId = wordSetId;
    }

    @Override
    protected void exec(DictionaryService service) {
        service.removeWordSet(wordSetId);
    }
}
