package messageSystem.messages.trainings.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.trainings.toServlet.MsgToGetRandomWordsServlet;
import services.db.TrainingService;

/**
 * Сообщение направленное на получение случайных слов
 */
public class MsgToGetRandomWords extends MsgToTrainingService {
    private final String sessionId;

    public MsgToGetRandomWords(Address from, Address to, String sessionId) {
        super(from, to);
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(TrainingService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MsgToGetRandomWordsServlet(getTo(), getFrom(),
                service.getRandomWords()), sessionId);
    }
}
