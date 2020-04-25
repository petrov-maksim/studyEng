package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import services.db.DictionaryService;

/**
 * Сообщение направленное на удаление перевода
 */
public class MsgRemoveTranslation extends MsgToDictionaryService {
    private int userId;
    private int wordId;
    private String translation;
    public MsgRemoveTranslation(Address from, Address to, int userId, int wordId, String translation) {
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
