package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import services.db.DictionaryService;

public class MessageRemoveWordsFromWordSet extends MessageToDictionaryService {
    private Integer wordIds[];
    private int wordSetId;
    private String sessionId;
    public MessageRemoveWordsFromWordSet(Address from, Address to, Integer[] wordIds, int wordSetId, String sessionId) {
        super(from, to);
        this.wordIds = wordIds;
        this.wordSetId = wordSetId;
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(DictionaryService service) {
        service.removeWordsFromWordSet(wordIds, wordSetId);
    }
}
