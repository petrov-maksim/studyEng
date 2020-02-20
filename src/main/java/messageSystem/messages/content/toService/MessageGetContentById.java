package messageSystem.messages.content.toService;

import entities.content.ContentTypes;
import messageSystem.Address;
import services.db.ContentService;

public class MessageGetContentById extends MessageToContentService {
    private final String sessionId;
    private final ContentTypes type;
    private final int contentId;
    public MessageGetContentById(Address from, Address to, String sessionId, ContentTypes type, int contentId) {
        super(from, to);
        this.sessionId = sessionId;
        this.type = type;
        this.contentId = contentId;
    }

    @Override
    protected void exec(ContentService service){

    }
}
