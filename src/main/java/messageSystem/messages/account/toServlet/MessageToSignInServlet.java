package messageSystem.messages.account.toServlet;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.account.SignInServlet;

public class MessageToSignInServlet extends Message {
    private boolean isAuthorized;
    public MessageToSignInServlet(Address from, Address to, boolean isAuthorized) {
        super(from, to);
        this.isAuthorized = isAuthorized;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof SignInServlet)
            exec((SignInServlet) abonent);
    }

    private void exec(SignInServlet servlet){
        servlet.handle(isAuthorized);
    }
}
