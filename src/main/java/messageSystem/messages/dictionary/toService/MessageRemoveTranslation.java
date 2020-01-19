package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import services.db.DictionaryService;

public class MessageRemoveTranslation extends MessageToDictionaryService {
    private int userId;
    private int wordId;
    private String translation;
    public MessageRemoveTranslation(Address from, Address to, int userId, int wordId, String translation) {
        super(from, to);
        this.userId = userId;
        this.wordId = wordId;
        this.translation = translation;
    }

    @Override
    protected void exec(DictionaryService service) {
        service.removeTranslationForUser(userId, wordId, translation);
    }
}
