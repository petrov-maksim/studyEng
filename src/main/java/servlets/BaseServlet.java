package servlets;

import messageSystem.Abonent;
import messageSystem.MessageSystem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BaseServlet extends Abonent {
    String READY = "ready";
    String ALREADY_AUTHORIZED = "authorized";
    /**
     * Create message for particular service and sends to MessageSystem
     */
     void createMessage();

     default void checkServiceResult(){
         MessageSystem.INSTANCE.execForServlet(this);
     }

     String getSessionId();

     default void notReady(){}
}
