package messageSystem.messages.trainings.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.trainings.toServlet.MessageToGetAmountOfUnlearnedWordsServlet;
import services.db.TrainingService;

public class MessageToGetAmountOfUnlearnedWords extends MessageToTrainingService {
    private final int userId;
    private final String sessionId;
    public MessageToGetAmountOfUnlearnedWords(Address from, Address to, int userId, String sessionId) {
        super(from, to);
        this.userId = userId;
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(TrainingService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToGetAmountOfUnlearnedWordsServlet(getTo(), getFrom(),
                service.getAmountOfUnlearnedWords(userId)),sessionId);
    }
}
