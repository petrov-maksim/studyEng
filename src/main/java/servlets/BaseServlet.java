package servlets;

import messageSystem.Abonent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BaseServlet extends Abonent {
    /**
     * Create message for particular service and sends to MessageSystem
     */
     void createMessage();

     void checkServiceResult();

    String getSessionId();
}
