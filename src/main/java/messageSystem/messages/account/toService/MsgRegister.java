package messageSystem.messages.account.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.account.toServlet.MsgToSignUpServlet;
import services.db.AccountService;

/**
 * Сообщение на регистрацию нового пользователя
 */
public class MsgRegister extends MsgToAccountService {
    private final String mail;
    private final String password;
    private final String name;
    private final String sessionId;

    public MsgRegister(Address from, Address to, String mail, String password, String name, String sessionId) {
        super(from, to);
        this.mail = mail;
        this.password = password;
        this.name = name;
        this.sessionId = sessionId;
    }

    @Override
    public void exec(AccountService service){
        MessageSystem.INSTANCE.sendMessageForServlet(new MsgToSignUpServlet(getTo(), getFrom(),
                service.signUp(mail, password, name, sessionId)), sessionId);
    }
}
