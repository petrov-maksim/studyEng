package messageSystem.messages.dictionary.toServlet;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.dictionary.AddTranslationServlet;

public class MessageToAddTranslationServlet extends Message {
    private int translationId;
    public MessageToAddTranslationServlet(Address from, Address to, int translationId) {
        super(from, to);
        this.translationId = translationId;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof AddTranslationServlet)
            exec((AddTranslationServlet) abonent);
    }

    private void exec(AddTranslationServlet servlet){
        servlet.handle(translationId);
    }
}
