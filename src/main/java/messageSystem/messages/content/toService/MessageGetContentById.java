package messageSystem.messages.content.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.content.toServlet.MessageToContentByIdServlet;
import services.db.ContentService;

public class MessageGetContentById extends MessageToContentService {
    private final String sessionId;
    private final String type;
    private final int contentId;
    public MessageGetContentById(Address from, Address to, String sessionId, String type, int contentId) {
        super(from, to);
        this.sessionId = sessionId;
        this.type = type;
        this.contentId = contentId;
    }

    @Override
    protected void exec(ContentService service){
        String payload = service.getContentById(contentId, type);
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToContentByIdServlet(getTo(), getFrom(), payload), sessionId);
    }
}
