package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import services.db.DictionaryService;

public class MessageAddWordsToWordSet extends MessageToDictionaryService {
    private Integer wordIds [];
    private int wordSetId;
    public MessageAddWordsToWordSet(Address from, Address to, Integer wordIds [], int wordSetId) {
        super(from, to);
        this.wordIds = wordIds;
        this.wordSetId = wordSetId;
    }

    @Override
    protected void exec(DictionaryService service) {
        service.addWordsToWordSet(wordIds, wordSetId);
    }
}
