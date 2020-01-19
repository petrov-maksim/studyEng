package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import services.db.DictionaryService;

public class MessageRemoveWordSet extends MessageToDictionaryService {
    private int wordSetId;
    public MessageRemoveWordSet(Address from, Address to, int wordSetId) {
        super(from, to);
        this.wordSetId = wordSetId;
    }

    @Override
    protected void exec(DictionaryService service) {
        service.removeWordSet(wordSetId);
    }
}
