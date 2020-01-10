package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import services.db.DictionaryService;

public class MessageAddWordToWordSet extends MessageToDictionaryService {
    private int userId;
    private Integer wordIds [];
    private int wordSetId;
    public MessageAddWordToWordSet(Address from, Address to,  int userId, Integer wordIds [], int wordSetId) {
        super(from, to);
        this.userId = userId;
        this.wordIds = wordIds;
        this.wordSetId = wordSetId;
    }

    @Override
    protected void exec(DictionaryService service) {
        service.addWordToWordSet(wordIds, userId, wordSetId);
    }
}
