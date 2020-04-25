package servlets;

import messageSystem.Abonent;
import messageSystem.MessageSystem;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Base class for servlets-abonent i.e. servlets which are waiting for result on request
 */
public abstract class ServletAbonent extends HttpServlet implements Abonent {
    protected static final String READY = "ready";

    /**
     * Create message for particular service and sends to MessageSystem
     */
    abstract protected void createMessage();

    /**
     * Checks whether result is ready for request
     */
    protected void checkServiceResult(){
        MessageSystem.INSTANCE.execForServlet(this);
    }

    public abstract String getSessionId();

    /**
     * If result isn't ready yet
     */
    public abstract void notReady();
}
