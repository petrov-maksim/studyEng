package messageSystem.messages.dictionary.toServlet;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.dictionary.GetTranslationsForWordServlet;

import java.util.Map;

public class MessageToGetTranslationsForWordServlet extends Message {
    private Map<Integer, String> translations;
    public MessageToGetTranslationsForWordServlet(Address from, Address to, Map<Integer, String> translations) {
        super(from, to);
        this.translations = translations;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GetTranslationsForWordServlet)
            exec((GetTranslationsForWordServlet) abonent);

    }

    private void exec(GetTranslationsForWordServlet servlet){
        servlet.handle(translations);
    }
}
