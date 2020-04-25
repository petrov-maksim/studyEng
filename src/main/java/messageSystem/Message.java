package messageSystem;

/**
 * Класс - сообщение, объекты котрого, могут быть использованны для отправки от абонента - к абоненту
 */
public abstract class Message {
    //Адрес отправителя сообщения
    private final Address from;
    // Адрес получателя сообщения
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

    /**
     * Код, который выполнит получатель
     * @param abonent - получатель, функционал, которого будет вызываться
     */
    public abstract void exec(Abonent abonent);
}
