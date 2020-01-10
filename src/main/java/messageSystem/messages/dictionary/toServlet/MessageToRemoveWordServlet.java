package messageSystem.messages.dictionary.toServlet;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.dictionary.user.RemoveWordForUserServlet;

public class MessageToRemoveWordServlet extends Message {
    private boolean status;
    public MessageToRemoveWordServlet(Address from, Address to, boolean status) {
        super(from, to);
        this.status = status;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof RemoveWordForUserServlet)
            exec((RemoveWordForUserServlet) abonent);
        else
            //logging
            System.out.println("Wrong abonent in MessageToRemoveServlet");
    }

    private void exec(RemoveWordForUserServlet servlet){
        servlet.handle(status);
    }
}
