package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import services.db.DictionaryService;

public class MessageUpdateTranslation extends MessageToDictionaryService {
    private final int userId;
    private final int wordId;
    private final int oldTranslationId;
    private final String newTranslation;
    public MessageUpdateTranslation(Address from, Address to, int userId, int wordId, int oldTranslationId, String newTranslation) {
        super(from, to);
        this.userId = userId;
        this.wordId = wordId;
        this.oldTranslationId = oldTranslationId;
        this.newTranslation = newTranslation;
    }

    @Override
    protected void exec(DictionaryService service) {
        service.updateTranslation(userId, wordId, oldTranslationId, newTranslation);
    }
}
