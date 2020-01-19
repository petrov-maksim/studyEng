package messageSystem;


import servlets.BaseServlet;
import util.AddressService;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Та самая система, которю будут шарить между собой потоки
 */
public enum MessageSystem {
    INSTANCE;
    /**
     * Message container
     * Сама мапа заполняется до запуска потоков, поэтому можно использовать не потокобузопасную.
     * Создаем объекты отоков, берем у них адрес, складываем их в эту карту и только после этого запускаем
     * и только после этого, они начинают обмениваться сообщениями, это будет происходить в main
     */
    private static final Map<Address, ConcurrentLinkedQueue<Message>> serviceMessages = new HashMap<>();

    private static final Map<Address, ConcurrentHashMap<String, Message>> servletMessages = new HashMap<>();

    static {
        serviceMessages.put(AddressService.INSTANCE.getAccountServiceAddress(), new ConcurrentLinkedQueue<>());
        serviceMessages.put(AddressService.INSTANCE.getContentServiceAddress(), new ConcurrentLinkedQueue<>());
        serviceMessages.put(AddressService.INSTANCE.getDictionaryService(), new ConcurrentLinkedQueue<>());


        servletMessages.put(AddressService.INSTANCE.getSignInServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getSignUpServletAddress(), new ConcurrentHashMap<>());

        servletMessages.put(AddressService.INSTANCE.getContentServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getContentByIdServletAddress(), new ConcurrentHashMap<>());

        servletMessages.put(AddressService.INSTANCE.getGetWordForUserServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getGetWordFromWordSetServletAddress(), new ConcurrentHashMap<>());

        servletMessages.put(AddressService.INSTANCE.getAddWordForUserServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getAddWordToWordSetServletAddress(), new ConcurrentHashMap<>());

        servletMessages.put(AddressService.INSTANCE.getGetWordSetsServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getAddWordSetServletAddress(), new ConcurrentHashMap<>());

        servletMessages.put(AddressService.INSTANCE.getGetTranslationsForWordServletAddress(), new ConcurrentHashMap<>());
    }

    public void sendMessageForService(Message message){
        serviceMessages.get(message.getTo()).add(message);
    }

    public void sendMessageForServlet(Message message, String sessionId){
        servletMessages.get(message.getTo()).put(sessionId, message);
    }


    public void execForService(Abonent abonent){
        Queue<Message> messageQueue = serviceMessages.get(abonent.getAddress());

        while(!messageQueue.isEmpty()){
            Message msg = messageQueue.poll();
            msg.exec(abonent);
        }
    }

    public void execForServlet(Abonent abonent){
        BaseServlet servlet = (BaseServlet) abonent;
        ConcurrentHashMap<String, Message> messages = servletMessages.get(abonent.getAddress());

        if(messages.containsKey(servlet.getSessionId()))
            messages.remove(servlet.getSessionId()).exec(abonent);
    }
}
