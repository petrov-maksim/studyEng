package servlets.grammar;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.grammar.toService.MsgGetCurrentLvl;
import servlets.ServletAbonent;
import util.AddressService;
import util.SessionCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервлет, обрабатывающий запрос на получение текущего грамматического уровня
 */
public class GetCurrentLvlServlet extends ServletAbonent {
    private static final Address address = new Address();
    private HttpServletResponse response;
    private String sessionId;
    private int userId;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        response = resp;
        sessionId = req.getSession().getId();
        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        //First request
        if (req.getHeader("handling") == null) {
            userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
            createMessage();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.flushBuffer();
        }
        //Not the first request
        else
            checkServiceResult();
    }

    @Override
    public void createMessage() {
        MessageSystem.INSTANCE.sendMessageForService(new MsgGetCurrentLvl(getAdr(), AddressService.INSTANCE.getGrammarServiceAddress(),
                userId, sessionId));
    }

    public void handle(String lvl){
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(READY, "true");

        try {
            if (lvl == null)
                response.flushBuffer();
            else {
                response.getWriter().write(lvl);
                response.flushBuffer();
            }
        } catch (IOException e) {
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
        }
    }

    @Override
    public void notReady() {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader(READY, "false");
            response.flushBuffer();
        } catch (IOException e) {
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
        }
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public Address getAddress() {
        return getAdr();
    }

    public static Address getAdr(){return address;}
}
