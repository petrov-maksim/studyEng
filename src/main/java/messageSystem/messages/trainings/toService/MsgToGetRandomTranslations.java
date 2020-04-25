package messageSystem.messages.trainings.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.trainings.toServlet.MsgToGetRandomTranslationsServlet;
import services.db.TrainingService;

/**
 * Сообщение направленное на получение случайных переводов
 */
public class MsgToGetRandomTranslations extends MsgToTrainingService {
    private final String sessionId;

    public MsgToGetRandomTranslations(Address from, Address to, String sessionId) {
        super(from, to);
        this.sessionId = sessionId;
    }

    @Override
    protected void exec(TrainingService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MsgToGetRandomTranslationsServlet(getTo(), getFrom(),
                service.getRandomTranslations()), sessionId);
    }
}
