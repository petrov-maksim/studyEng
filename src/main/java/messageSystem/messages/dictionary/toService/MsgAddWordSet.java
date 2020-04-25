package messageSystem.messages.dictionary.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toServlet.MsgToAddWordSetServlet;
import org.apache.commons.fileupload.FileItem;
import services.db.DictionaryService;

/**
 * Сообщение добавления набора слов
 */
public class MsgAddWordSet extends MsgToDictionaryService {
    private final String name;
    private final String sessionId;
    private final int userId;
    private final FileItem img;

    public MsgAddWordSet(Address from, Address to, FileItem img, String name, String sessionId, int userId) {
        super(from, to);
        this.name = name;
        this.sessionId = sessionId;
        this.userId = userId;
        this.img = img;
    }

    @Override
    protected void exec(DictionaryService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MsgToAddWordSetServlet(getTo(),getFrom(),
                service.addWordSetForUser(img, name, userId)), sessionId);
    }
}
