package messageSystem.messages.content.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.content.toServlet.MessageToGetAllContentVideosServlet;
import services.db.ContentService;

public class MessageGetAllContentVideos extends MessageToContentService {
    private final String sessionId;
    private final int userId;
    private final int index;
    public MessageGetAllContentVideos(Address from, Address to, String sessionId, int userId, int index) {
        super(from, to);
        this.sessionId = sessionId;
        this.userId = userId;
        this.index = index;
    }

    @Override
    protected void exec(ContentService service){
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToGetAllContentVideosServlet(getTo(), getFrom(),
                service.getAllContentVideos(userId, index)), sessionId);
    }
}
