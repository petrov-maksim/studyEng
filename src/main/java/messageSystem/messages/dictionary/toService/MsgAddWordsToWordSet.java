package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import services.db.DictionaryService;
import java.util.List;

/**
 * Сообщение добавления слов в набор
 */
public class MsgAddWordsToWordSet extends MsgToDictionaryService {
    private List<Integer> wordIds;
    private int wordSetId;

    public MsgAddWordsToWordSet(Address from, Address to, List<Integer> wordIds, int wordSetId) {
        super(from, to);
        this.wordIds = wordIds;
        this.wordSetId = wordSetId;
    }

    @Override
    protected void exec(DictionaryService service) {
        service.addWordsToWordSet(wordIds, wordSetId);
    }
}
