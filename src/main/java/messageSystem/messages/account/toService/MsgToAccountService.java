package messageSystem.messages.account.toService;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import services.db.AccountService;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Базовый класс сообщения в AccountService
 */
public abstract class MsgToAccountService extends Message {
    public MsgToAccountService(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof AccountService)
            exec((AccountService) abonent);
        else
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Wrong abonent");
    }

    public abstract void exec(AccountService service);
}
