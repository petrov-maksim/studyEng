package messageSystem.messages.account;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
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
    public void exec(Abonent abonent) {
        if (abonent instanceof AccountService)
            exec((AccountService) abonent);
        else
            System.out.println("Wrong Abonent in MessageRegister");
    }

    private void exec(AccountService service){
        boolean status = service.register(mail, password, name, sessionId);
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToSignUpServlet(getTo(), getFrom(), status), sessionId);
    }
}
