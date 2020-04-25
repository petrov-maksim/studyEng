package messageSystem.messages.account.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.account.toServlet.MsgToSignInServlet;
import services.db.AccountService;

/**
 * Сообщение на аутентификацию пользователя
 */
public class MsgAuthenticate extends MsgToAccountService {
    private String mail;
    private String password;
    private String sessionId;

    public MsgAuthenticate(Address from, Address to, String mail, String password, String sessionId) {
        super(from, to);
        this.mail = mail;
        this.password = password;
        this.sessionId = sessionId;
    }

    @Override
    public void exec(AccountService service){
        MessageSystem.INSTANCE.sendMessageForServlet(new MsgToSignInServlet(getTo(), getFrom(),
                service.signIn(mail, password, sessionId)),sessionId);
    }
}
