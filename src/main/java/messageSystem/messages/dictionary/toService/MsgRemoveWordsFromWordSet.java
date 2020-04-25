package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import services.db.DictionaryService;

/**
 * Сообщение направленное на удаление слов из набора
 */
public class MsgRemoveWordsFromWordSet extends MsgToDictionaryService {
    private Integer wordIds[];
    private int wordSetId;

    public MsgRemoveWordsFromWordSet(Address from, Address to, Integer[] wordIds, int wordSetId) {
        super(from, to);
        this.wordIds = wordIds;
        this.wordSetId = wordSetId;
    }

    @Override
    protected void exec(DictionaryService service) {
        service.removeWordsFromWordSet(wordIds, wordSetId);
    }
}
