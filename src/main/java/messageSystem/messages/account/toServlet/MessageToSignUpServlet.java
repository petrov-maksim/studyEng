package messageSystem.messages.account.toServlet;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import servlets.account.SignUpServlet;

public class MessageToSignUpServlet extends Message {
    private final boolean isRegistered;
    public MessageToSignUpServlet(Address from, Address to, boolean isRegistered) {
        super(from, to);
        this.isRegistered = isRegistered;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof SignUpServlet)
            exec((SignUpServlet) abonent);
    }

    private void exec(SignUpServlet servlet){
        servlet.handle(isRegistered);
    }
}
