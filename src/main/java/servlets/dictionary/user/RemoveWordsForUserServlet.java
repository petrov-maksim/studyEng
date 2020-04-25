package servlets.dictionary.user;

import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MsgRemoveWordsForUser;
import servlets.NonAbonentServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервлет обрабатывающий запрос на удаление слов для пользователя.
 */
public class RemoveWordsForUserServlet extends HttpServlet implements NonAbonentServlet {
    private String sessionId;
    private Integer wordIds[];
    private int userId;

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
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "word ids aren't valid: " +
                    Arrays.toString(wordIds), e);
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
        MessageSystem.INSTANCE.sendMessageForService(new MsgRemoveWordsForUser(null, AddressService.INSTANCE.getDictionaryServiceAddress(),
                wordIds, userId));
    }

    private void initParams(HttpServletRequest request){
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
        wordIds = Arrays.stream(request.getParameter("wordId").split(",")).
                map(Integer::parseInt).toArray(Integer[]::new);
    }
}
