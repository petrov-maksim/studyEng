package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toServlet.MessageToUpdateWordSetServlet;
import services.db.DictionaryService;

public class MessageUpdateWordSet extends MessageToDictionaryService {
    private String newName;
    private int wordSetId;

    public MessageUpdateWordSet(Address from, Address to, String newName, int wordSetId) {
        super(from, to);
        this.newName = newName;
        this.wordSetId = wordSetId;
    }

    @Override
    protected void exec(DictionaryService service) {
        service.updateWordSetName(newName,  wordSetId);
    }
}
