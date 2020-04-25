package messageSystem.messages.account.toServlet;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.account.SignUpServlet;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MsgToSignUpServlet extends Message {
    private final boolean isRegistered;
    public MsgToSignUpServlet(Address from, Address to, boolean isRegistered) {
        super(from, to);
        this.isRegistered = isRegistered;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof SignUpServlet)
            exec((SignUpServlet) abonent);
        else
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Wrong abonent");
    }

    private void exec(SignUpServlet servlet){
        servlet.handle(isRegistered);
    }
}
