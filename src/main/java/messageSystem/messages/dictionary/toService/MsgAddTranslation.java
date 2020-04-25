package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import services.db.DictionaryService;

/**
 * Сообщение добавления перевода к слову
 */
public class MsgAddTranslation extends MsgToDictionaryService {
    private String translation;
    private int wordId;
    private int userId;
    public MsgAddTranslation(Address from, Address to, String translation, int wordId, int userId) {
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
