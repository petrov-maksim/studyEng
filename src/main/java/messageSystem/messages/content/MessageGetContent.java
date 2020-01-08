package messageSystem.messages.content;

import messageSystem.Address;
import messageSystem.MessageSystem;
import services.db.ContentService;

public class MessageGetContent extends MessageToContentService {
    private int nVideos;
    private int nTexts;
    private String sessionId;
    public MessageGetContent(Address from, Address to, int nVideos, int nTexts, String sessionId) {
        super(from, to);
        this.nVideos = nVideos;
        this.nTexts = nTexts;
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(ContentService service){
        String videos [] = new String[nVideos];
        String texts [] = new String[nTexts];
        if (nVideos != 0)
            videos = service.getNVideos(nVideos);
        if (nTexts != 0)
            texts = service.getNTexts(nTexts);

        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToContentServlet(getTo(), getFrom(), videos, texts), sessionId);
    }
}
