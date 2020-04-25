package messageSystem.messages.trainings.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.trainings.toServlet.MsgToGetUnlearnedWordsServlet;
import services.db.TrainingService;

/**
 * Сообщение направленное на получение неизученных слов
 */
public class MsgToGetUnlearnedWords extends MsgToTrainingService {
    private final String sessionId;
    private final int trainingId;
    private final int userId;

    public MsgToGetUnlearnedWords(Address from, Address to, String sessionId, int trainingId, int userId) {
        super(from, to);
        this.sessionId = sessionId;
        this.trainingId = trainingId;
        this.userId = userId;
    }

    @Override
    protected void exec(TrainingService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MsgToGetUnlearnedWordsServlet(getTo(), getFrom(),
                service.getUnlearnedWordsForTraining(trainingId, userId)), sessionId);
    }
}
