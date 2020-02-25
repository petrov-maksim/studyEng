package messageSystem.messages.trainings.toServlet;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.trainings.GetRandomWordsServlet;

import java.util.List;

public class MessageToGetRandomWordsServlet extends Message {
    private final List<String> words;
    public MessageToGetRandomWordsServlet(Address from, Address to, List<String> words) {
        super(from, to);
        this.words = words;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GetRandomWordsServlet)
            exec((GetRandomWordsServlet) abonent);
    }

    private void exec(GetRandomWordsServlet servlet){
        servlet.handle(words);
    }
}
