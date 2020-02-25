package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import services.db.DictionaryService;

import java.util.List;

public class MessageAddWordsToWordSet extends MessageToDictionaryService {
    private List<Integer> wordIds;
    private int wordSetId;
    public MessageAddWordsToWordSet(Address from, Address to, List<Integer> wordIds, int wordSetId) {
        super(from, to);
        this.wordIds = wordIds;
        this.wordSetId = wordSetId;
    }

    @Override
    protected void exec(DictionaryService service) {
        service.addWordsToWordSet(wordIds, wordSetId);
    }
}
