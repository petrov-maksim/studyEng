package messageSystem.messages.trainings.toService;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import services.db.DictionaryService;
import services.db.TrainingService;

public abstract class MessageToTrainingService  extends Message {
    public MessageToTrainingService(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof TrainingService)
            exec((TrainingService) abonent);
        else
            System.out.println("Wrong abonent in MessageToTrainingService");
    }

    protected abstract void exec(TrainingService service);
}
