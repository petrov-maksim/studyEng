package messageSystem.messages.account.toServlet;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.account.SignInServlet;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MsgToSignInServlet extends Message {
    private boolean isAuthorized;
    public MsgToSignInServlet(Address from, Address to, boolean isAuthorized) {
        super(from, to);
        this.isAuthorized = isAuthorized;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof SignInServlet)
            exec((SignInServlet) abonent);
        else
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Wrong abonent");
    }

    private void exec(SignInServlet servlet){
        servlet.handle(isAuthorized);
    }
}
