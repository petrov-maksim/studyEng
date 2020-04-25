package messageSystem.messages.trainings.toService;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import services.db.DictionaryService;
import services.db.TrainingService;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Базовый класс сообщения в TrainingService
 */
public abstract class MsgToTrainingService extends Message {
    public MsgToTrainingService(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof TrainingService)
            exec((TrainingService) abonent);
        else
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Wrong abonent");
    }

    protected abstract void exec(TrainingService service);
}
