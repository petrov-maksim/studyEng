package messageSystem.messages.account;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.account.SignInServlet;

public class MessageToSignInServlet extends Message {
    public MessageToSignInServlet(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof SignInServlet)
            exec((SignInServlet) abonent);
    }

    private void exec(SignInServlet servlet){
        servlet.userNotAuthorized();
    }
}
