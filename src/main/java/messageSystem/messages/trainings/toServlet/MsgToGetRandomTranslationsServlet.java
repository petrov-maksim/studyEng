package messageSystem.messages.trainings.toServlet;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.trainings.GetRandomTranslationsServlet;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сообщение с текущим случайными переводами
 */
public class MsgToGetRandomTranslationsServlet extends Message {
    private final List<String> translations;

    public MsgToGetRandomTranslationsServlet(Address from, Address to, List<String> translations) {
        super(from, to);
        this.translations = translations;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof GetRandomTranslationsServlet)
            exec((GetRandomTranslationsServlet) abonent);
        else
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Wrong abonent");
    }

    private void exec(GetRandomTranslationsServlet servlet){
        servlet.handle(translations);
    }
}
