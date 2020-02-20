package servlets.dictionary.user;

import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MessageRemoveWordsForUser;
import servlets.NonAbonentServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * TODO: Добавить разбиение на порции
 * На фронтенде, если слов больше, чем пороговое значение, то разбить вызовы на сервер, в несколько заходов
 * На данный момент, word's ids лежат в заголовке:
 * wordId: 1,2,3,4,5
 */

public class RemoveWordsForUserServlet extends HttpServlet implements NonAbonentServlet {
    private String sessionId;
    private Integer wordIds[];
    private int userId;


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sessionId = req.getSession().getId();

        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        try{
            initParams(req);
        }catch (Exception e){
            //logging
            System.out.println("Wrong parameters in RemoveWordForUserServlet");
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
        MessageSystem.INSTANCE.sendMessageForService(new MessageRemoveWordsForUser(null, AddressService.INSTANCE.getDictionaryServiceAddress(),
                wordIds, userId, sessionId));
    }

    private void initParams(HttpServletRequest request){
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
        wordIds = Arrays.stream(request.getParameter("wordId").split(",")).
                map(Integer::parseInt).toArray(Integer[]::new);

        System.out.println(Arrays.toString(wordIds));
    }
}
