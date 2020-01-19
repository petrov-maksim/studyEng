package services;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;

import java.util.ArrayList;
import java.util.List;

/**
 * У каждого сервиса-типа имеется один уникальный адрес и несколько тредов
 * У каждого потока дата-базного сервиса, имеется один уникальный QueryExecutor, у которого уникальное сединение к базе
 * Сервисы выполняют строго свои задачи, работа по валидации данных, будет проходить в сервлетах
 */
public abstract class AbstractService implements Abonent {
    private final Address address;
    private final List<Thread> threads;

    public AbstractService(int threadsNum) {
        address = new Address();
        threads = new ArrayList<>();
        initThreads(threadsNum);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    private void initThreads(int num){
        for (int i = 0; i < num; i++) {
            Thread t = new Thread(() -> {
                while(true){
                    try{
                        MessageSystem.INSTANCE.execForService(this);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            threads.add(t);
        }
    }

    public List<Thread> getThreads(){
        return threads;
    }
}
