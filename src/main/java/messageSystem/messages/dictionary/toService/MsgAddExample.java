package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import services.db.DictionaryService;

/**
 * Сообщение добавления примера к слову
 */
public class MsgAddExample extends MsgToDictionaryService {
    private String example;
    private int userId;
    private int wordId;
    public MsgAddExample(Address from, Address to, String example, int userId, int wordId) {
        super(from, to);
        this.example = example;
        this.userId = userId;
        this.wordId = wordId;
    }

    @Override
    protected void exec(DictionaryService service) {
        service.addExampleForUser(userId, wordId, example);
    }
}
