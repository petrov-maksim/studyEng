package messageSystem;

import messageSystem.Abonent;
import messageSystem.Address;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;

public abstract class Message {
    private final Address from;
    private final Address to;

    public Message(Address from, Address to) {
        this.from = from;
        this.to = to;
    }

    protected Address getFrom() {
        return from;
    }

    protected Address getTo() {
        return to;
    }

    public abstract void exec(Abonent abonent);
}
