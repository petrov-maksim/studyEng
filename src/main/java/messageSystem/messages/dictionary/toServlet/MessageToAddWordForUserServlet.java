package messageSystem.messages.dictionary.toServlet;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.dictionary.user.AddWordsForUserServlet;

public class MessageToAddWordForUserServlet extends Message {
    private int wordId;
    public MessageToAddWordForUserServlet(Address from, Address to, int wordId) {
        super(from, to);
        this.wordId = wordId;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof AddWordsForUserServlet)
            exec((AddWordsForUserServlet) abonent);
        else
            System.out.println("wrong abonent in MessageToAddWordServlet");
    }

    private void exec(AddWordsForUserServlet servlet){
        servlet.handle(wordId);
    }
}
