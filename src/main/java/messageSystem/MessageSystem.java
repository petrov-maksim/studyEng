package messageSystem;

import servlets.ServletAbonent;
import util.AddressService;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Класс обеспечивающий обмен сообщениями между абонентами
 */
public enum MessageSystem {
    INSTANCE;

    /**
     * У каждого сервиса, вне зависимости от колличества тредов, есть 1 уникальный статичный адрес,
     * этому адресу соответствует очередь сообщений
     */
    private static final Map<Address, ConcurrentLinkedQueue<Message>> serviceMessages = new HashMap<>();

    /**
     * У каждого сервлета, ожидающего результат, есть уникальный статический адрес,
     * этому адресу соответствует мапа с sessionId-message
     */
    private static final Map<Address, ConcurrentHashMap<String, Message>> servletMessages = new HashMap<>();

    static {
        serviceMessages.put(AddressService.INSTANCE.getAccountServiceAddress(), new ConcurrentLinkedQueue<>());
        serviceMessages.put(AddressService.INSTANCE.getDictionaryServiceAddress(), new ConcurrentLinkedQueue<>());
        serviceMessages.put(AddressService.INSTANCE.getTrainingServiceAddress(), new ConcurrentLinkedQueue<>());
        serviceMessages.put(AddressService.INSTANCE.getGrammarServiceAddress(), new ConcurrentLinkedQueue<>());

        servletMessages.put(AddressService.INSTANCE.getSignInServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getSignUpServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getGetWordSetsServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getAddWordSetServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getGetWordForUserServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getGetWordFromWordSetServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getAddWordForUserServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getGetAmountOfUnlearnedWordsServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getGetRandomTranslationsServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getGetRandomWordsServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getGetUnlearnedWordsServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getGetCurrentLvlServletAddress(), new ConcurrentHashMap<>());
    }

    /**
     * Отправка сообщения сервису
     */
    public void sendMessageForService(Message message){
        serviceMessages.get(message.getTo()).add(message);
    }

    /**
     * Отправка сообщения сервлету
     */
    public void sendMessageForServlet(Message message, String sessionId){
        servletMessages.get(message.getTo()).put(sessionId, message);
    }

    /**
     * Выполнение сообщения сервисом
     */
    public void execForService(Abonent abonent){
        Queue<Message> messageQueue = serviceMessages.get(abonent.getAddress());
        Message msg;
        for (msg = messageQueue.poll(); msg != null; msg = messageQueue.poll())
            msg.exec(abonent);
    }

    /**
     * Выполнение сообщения сервлетом
     */
    public void execForServlet(Abonent abonent){
        ServletAbonent servlet = (ServletAbonent) abonent;
        ConcurrentHashMap<String, Message> messages = servletMessages.get(abonent.getAddress());

        if(messages.containsKey(servlet.getSessionId()))
            messages.remove(servlet.getSessionId()).exec(abonent);
        else
            servlet.notReady();
    }
}
