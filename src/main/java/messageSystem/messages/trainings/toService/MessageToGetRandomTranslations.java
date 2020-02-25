package messageSystem.messages.trainings.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.trainings.toServlet.MessageToGetRandomTranslationsServlet;
import services.db.TrainingService;

public class MessageToGetRandomTranslations extends MessageToTrainingService {
    private final String sessionId;
    public MessageToGetRandomTranslations(Address from, Address to, String sessionId) {
        super(from, to);
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(TrainingService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToGetRandomTranslationsServlet(getTo(), getFrom(),
                service.getRandomTranslations()), sessionId);
    }
}
