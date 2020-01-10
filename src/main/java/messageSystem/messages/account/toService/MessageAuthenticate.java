package messageSystem.messages.account.toService;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import messageSystem.MessageSystem;
import messageSystem.messages.account.toServlet.MessageToSignInServlet;
import services.db.AccountService;


public class MessageAuthenticate extends MessageToAccountService {
    private String mail;
    private String password;
    private String sessionId;

    public MessageAuthenticate(Address from, Address to, String mail, String password, String sessionId) {
        super(from, to);
        this.mail = mail;
        this.password = password;
        this.sessionId = sessionId;
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof AccountService)
            exec((AccountService) abonent);
        else
            //Replace on logging
            System.out.println("Wrong Abonent");
    }

    private void exec(AccountService service){
        if (!service.authorize(mail, password, sessionId)){
            Message back = new MessageToSignInServlet(getTo(), getFrom());
            MessageSystem.INSTANCE.sendMessageForServlet(back,sessionId);
        }
    }
}
