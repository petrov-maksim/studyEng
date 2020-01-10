package messageSystem.messages.content.toService;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import services.db.ContentService;

public abstract class MessageToContentService extends Message {
    public MessageToContentService(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof ContentService)
            exec((ContentService) abonent);
    }

    protected abstract void exec(ContentService service);
}
