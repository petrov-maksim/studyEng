package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import services.db.DictionaryService;

public class MessageAddTranslation extends MessageToDictionaryService {
    private String translation;
    private int wordId;
    private int userId;
    public MessageAddTranslation(Address from, Address to, String translation,  int wordId, int userId) {
        super(from, to);
        this.translation = translation;
        this.wordId = wordId;
        this.userId = userId;
    }

    @Override
    protected void exec(DictionaryService service) {
        service.addTranslationForUser(wordId, translation, userId);
    }
}
