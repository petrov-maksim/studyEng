package servlets.dictionary.wordSet;

import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MsgRemoveWordSet;
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
 * Сервлет, обрабатывающий запрос на удаление набора слов
 */
public class RemoveWordSetServlet extends HttpServlet implements NonAbonentServlet {
    private int wordSetId;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String sessionId = req.getSession().getId();

        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        try {
            initParams(req);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Invalid wordSetId: " + wordSetId, e);
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
        MessageSystem.INSTANCE.sendMessageForService(new MsgRemoveWordSet(null,
                AddressService.INSTANCE.getDictionaryServiceAddress(), wordSetId));
    }


    private void initParams(HttpServletRequest request) {
        wordSetId = Integer.parseInt(request.getHeader("wordSetId"));
    }
}
