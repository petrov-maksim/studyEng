package messageSystem.messages.trainings.toServlet;

import entities.Training;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.trainings.GetAmountOfUnlearnedWordsServlet;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сообщение с количеством неизученных слов
 */
public class MsgToGetAmountOfUnlearnedWordsServlet extends Message {
    private final Collection<Training> trainings;

    public MsgToGetAmountOfUnlearnedWordsServlet(Address from, Address to, Collection<Training> trainings) {
        super(from, to);
        this.trainings = trainings;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GetAmountOfUnlearnedWordsServlet)
            exec((GetAmountOfUnlearnedWordsServlet) abonent);
        else
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Wrong abonent");
    }

    private void exec(GetAmountOfUnlearnedWordsServlet servlet){
        servlet.handle(trainings);
    }
}
