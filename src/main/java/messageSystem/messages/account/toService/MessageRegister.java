package messageSystem.messages.account.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.account.toServlet.MessageToSignUpServlet;
import services.db.AccountService;

public class MessageRegister extends MessageToAccountService {
    private final String mail;
    private final String password;
    private final String name;
    private final String sessionId;

    public MessageRegister(Address from, Address to, String mail, String password, String name, String sessionId) {
        super(from, to);
        this.mail = mail;
        this.password = password;
        this.name = name;
        this.sessionId = sessionId;
    }

    @Override
    public void exec(AccountService service){
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToSignUpServlet(getTo(), getFrom(),
                service.signUp(mail, password, name, sessionId)), sessionId);
    }
}
