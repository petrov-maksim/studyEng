package messageSystem.messages.trainings.toService;

import messageSystem.Address;
import services.db.TrainingService;

import java.util.List;

/**
 * Сообщение направленное на перевод слова в состояние "на изучении"
 */
public class MsgToMoveWordsToLearning extends MsgToTrainingService {
    private final int userId;
    private final int trainingId;
    private final List<Integer> wordIds;

    public MsgToMoveWordsToLearning(Address from, Address to, int userId, int trainingId, List<Integer> wordIds) {
        super(from, to);
        this.userId = userId;
        this.trainingId = trainingId;
        this.wordIds = wordIds;
    }

    @Override
    protected void exec(TrainingService service) {
        service.moveWordsToLearning(userId, wordIds, trainingId);
    }
}
