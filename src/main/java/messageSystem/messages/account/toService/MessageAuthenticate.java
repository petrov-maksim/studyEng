package messageSystem.messages.account.toService;

import messageSystem.Address;
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
    public void exec(AccountService service){
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToSignInServlet(getTo(), getFrom(),
                service.signIn(mail, password, sessionId)),sessionId);
    }
}
