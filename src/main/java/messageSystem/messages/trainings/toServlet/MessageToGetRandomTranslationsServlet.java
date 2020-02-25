package messageSystem.messages.trainings.toServlet;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.trainings.GetRandomTranslationsServlet;

import java.util.List;

public class MessageToGetRandomTranslationsServlet extends Message {
    private final List<String> translations;
    public MessageToGetRandomTranslationsServlet(Address from, Address to, List<String> translations) {
        super(from, to);
        this.translations = translations;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GetRandomTranslationsServlet)
            exec((GetRandomTranslationsServlet) abonent);
    }

    private void exec(GetRandomTranslationsServlet servlet){
        servlet.handle(translations);
    }
}
