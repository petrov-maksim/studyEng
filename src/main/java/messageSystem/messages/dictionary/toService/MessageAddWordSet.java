package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toServlet.MessageToAddWordSetServlet;
import org.apache.commons.fileupload.FileItem;
import services.db.DictionaryService;

public class MessageAddWordSet extends MessageToDictionaryService {
    private final String name;
    private final String sessionId;
    private final int userId;
    private final FileItem img;
    public MessageAddWordSet(Address from, Address to, FileItem img, String name, String sessionId, int userId) {
        super(from, to);
        this.name = name;
        this.sessionId = sessionId;
        this.userId = userId;
        this.img = img;
    }

    @Override
    protected void exec(DictionaryService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToAddWordSetServlet(getTo(),getFrom(),
                service.addWordSetForUser(img, name, userId)), sessionId);
    }
}
