package messageSystem.messages.trainings.toService;

import messageSystem.Address;
import services.db.TrainingService;

import java.util.List;

public class MessageToMoveWordsToLearning extends MessageToTrainingService{
    private final int userId;
    private final int trainingId;
    private final List<Integer> wordIds;

    public MessageToMoveWordsToLearning(Address from, Address to, int userId, int trainingId, List<Integer> wordIds) {
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
