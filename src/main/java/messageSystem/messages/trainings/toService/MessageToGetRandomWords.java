package messageSystem.messages.trainings.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.trainings.toServlet.MessageToGetRandomWordsServlet;
import services.db.TrainingService;

public class MessageToGetRandomWords extends MessageToTrainingService{
    private final String sessionId;
    public MessageToGetRandomWords(Address from, Address to, String sessionId) {
        super(from, to);
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(TrainingService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToGetRandomWordsServlet(getTo(), getFrom(),
                service.getRandomWords()), sessionId);
    }
}
