package messageSystem;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Уникальный адрес класса, учавствующего в рассылке/получении сообщений
 */
public class Address {
    private static final AtomicInteger idCreator = new AtomicInteger();
    private final int abonentId;

    public Address() {
        abonentId = idCreator.incrementAndGet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Address address = (Address) o;
        return abonentId == address.abonentId;
    }

    @Override
    public int hashCode() {
        return abonentId;
    }
}
