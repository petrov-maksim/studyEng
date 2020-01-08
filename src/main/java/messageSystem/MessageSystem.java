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
//        serviceMessages.put(AddressService.INSTANCE.getRegistryService(), new ConcurrentLinkedQueue<>());
//        serviceMessages.put(AddressService.INSTANCE.getContentService(), new ConcurrentLinkedQueue<>());
//        serviceMessages.put(AddressService.INSTANCE.getDictionaryService(), new ConcurrentLinkedQueue<>());
//        serviceMessages.put(AddressService.INSTANCE.getGrammarService(), new ConcurrentLinkedQueue<>());
//        serviceMessages.put(AddressService.INSTANCE.getTrainingsService(), new ConcurrentLinkedQueue<>());

        servletMessages.put(AddressService.INSTANCE.getSignInServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getSignUpServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getContentServletAddress(), new ConcurrentHashMap<>());
        servletMessages.put(AddressService.INSTANCE.getContentByIdServletAddress(), new ConcurrentHashMap<>());
//        messages.put(AddressService.INSTANCE.getAuthRH(), new ConcurrentLinkedQueue<>());
//        messages.put(AddressService.INSTANCE.getRegistryRH(), new ConcurrentLinkedQueue<>());
//        messages.put(AddressService.INSTANCE.getContentRH(), new ConcurrentLinkedQueue<>());
//        messages.put(AddressService.INSTANCE.getDictionaryRH(), new ConcurrentLinkedQueue<>());
//        messages.put(AddressService.INSTANCE.getGrammarRH(), new ConcurrentLinkedQueue<>());
//        messages.put(AddressService.INSTANCE.getTrainingsRH(), new ConcurrentLinkedQueue<>());
    }

    public void sendMessageForService(Message message){
        serviceMessages.get(message.getTo()).add(message);
    }

    public void sendMessageForServlet(Message message, String sessionId){
        servletMessages.get(message.getTo()).put(sessionId, message);
    }

    /**
     * У каждого сервиса ест ссылка на MessageSystem, каждый сервис можеты вызвать execForAbonent и передать себя в качестве абонента.
     * Таким образом, для каждого сообщения, которое пришло к сервису, сервис передает себя в exec этого сообщения
     */
    public void execForAbonent(Abonent abonent){
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
