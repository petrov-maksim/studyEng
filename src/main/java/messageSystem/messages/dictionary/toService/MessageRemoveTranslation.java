package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import services.db.DictionaryService;

public class MessageRemoveTranslation extends MessageToDictionaryService {
    private int userId;
    private int wordId;
    private int translationId;
    public MessageRemoveTranslation(Address from, Address to, int userId, int wordId, int translationId) {
        super(from, to);
        this.userId = userId;
        this.wordId = wordId;
        this.translationId = translationId;
    }

    @Override
    protected void exec(DictionaryService service) {
        service.removeTranslation(userId, wordId, translationId);
    }
}
