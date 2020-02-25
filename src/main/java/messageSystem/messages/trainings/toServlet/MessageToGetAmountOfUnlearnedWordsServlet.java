package messageSystem.messages.trainings.toServlet;

import entities.Training;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.trainings.GetAmountOfUnlearnedWordsServlet;

public class MessageToGetAmountOfUnlearnedWordsServlet extends Message {
    private final Training trainings[];
    public MessageToGetAmountOfUnlearnedWordsServlet(Address from, Address to, Training trainings []) {
        super(from, to);
        this.trainings = trainings;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GetAmountOfUnlearnedWordsServlet)
            exec((GetAmountOfUnlearnedWordsServlet) abonent);
        else
            System.out.println("Wrong abonent in MessageToGetAmountOfUnlearnedWordsServlet");
    }

    private void exec(GetAmountOfUnlearnedWordsServlet servlet){
        servlet.handle(trainings);
    }
}
