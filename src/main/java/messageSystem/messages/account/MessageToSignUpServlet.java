package messageSystem.messages.account;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.messages.account.MessageToAccountService;
import servlets.account.SignUpServlet;

public class MessageToSignUpServlet extends MessageToAccountService {
    private final boolean status;
    public MessageToSignUpServlet(Address from, Address to, boolean status) {
        super(from, to);
        this.status = status;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof SignUpServlet)
            exec((SignUpServlet) abonent);
    }

    private void exec(SignUpServlet servlet){
        if (status)
            servlet.userRegistered();
        else
            servlet.userNotRegistered();
    }
}
