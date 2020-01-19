package messageSystem.messages.account.toService;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import services.db.AccountService;

public abstract class MessageToAccountService extends Message {
    public MessageToAccountService(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Abonent abonent) {
        if (abonent instanceof AccountService)
            exec((AccountService) abonent);
        else
            //Replace on logging
            System.out.println("Wrong Abonent");
    }

    public abstract void exec(AccountService service);
}
