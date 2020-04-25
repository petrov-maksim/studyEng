package servlets.grammar;

import messageSystem.MessageSystem;
import messageSystem.messages.grammar.toService.MsgUpdateCurrentLvl;
import servlets.NonAbonentServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервлет, обрабатывающий запрос на обновление текущего грамматического уровня
 */
public class UpdateCurrentLvlServlet extends HttpServlet implements NonAbonentServlet {
    private String lvl;
    private int userId;
    private String sessionId;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        sessionId = req.getSession().getId();

        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        try{
            initParams(req);
        }catch (Exception e){
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.flushBuffer();
            return;
        }

        createMessage();
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.flushBuffer();
    }

    @Override
    public void createMessage() {
        MessageSystem.INSTANCE.sendMessageForService(new MsgUpdateCurrentLvl(null, AddressService.INSTANCE.getGrammarServiceAddress(),
                userId, lvl));
    }

    private void initParams(HttpServletRequest request) throws Exception {
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
        lvl = request.getParameter("lvl");

        if (lvl == null || lvl.length() > 6 || lvl.length() < 4)
            throw new Exception("Invalid grammar level: " + lvl);
    }
}
