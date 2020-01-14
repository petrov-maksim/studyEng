package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import services.db.DictionaryService;

public class MessageAddExample extends MessageToDictionaryService {
    private String example;
    private int userId;
    private int wordId;
    public MessageAddExample(Address from, Address to, String example, int userId, int wordId) {
        super(from, to);
        this.example = example;
        this.userId = userId;
        this.wordId = wordId;
    }

    @Override
    protected void exec(DictionaryService service) {
        service.addExample(userId, wordId, example);
    }
}
