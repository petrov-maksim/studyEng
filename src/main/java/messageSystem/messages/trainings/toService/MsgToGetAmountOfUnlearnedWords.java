package messageSystem.messages.trainings.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.trainings.toServlet.MsgToGetAmountOfUnlearnedWordsServlet;
import services.db.TrainingService;

/**
 * Сообщение направленное на получение количества не изученных слов
 */
public class MsgToGetAmountOfUnlearnedWords extends MsgToTrainingService {
    private final int userId;
    private final String sessionId;

    public MsgToGetAmountOfUnlearnedWords(Address from, Address to, int userId, String sessionId) {
        super(from, to);
        this.userId = userId;
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(TrainingService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MsgToGetAmountOfUnlearnedWordsServlet(getTo(), getFrom(),
                service.getAmountOfUnlearnedWords(userId)),sessionId);
    }
}
