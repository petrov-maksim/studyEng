package servlets.dictionary;

import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MessageAddExample;
import servlets.NonAbonentServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Используется для добавления, удаления и перезаписи примера
 */
public class AddExampleServlet extends HttpServlet implements NonAbonentServlet {
    private String sessionId;
    private String example;
    private int userId;
    private int wordId;


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
            System.out.println("Wrong parameters in AddExampleServlet");
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
        MessageSystem.INSTANCE.sendMessageForService(new MessageAddExample(null, AddressService.INSTANCE.getDictionaryService(),
                example, userId, wordId));
    }

    private void initParams(HttpServletRequest request) throws Exception {
        if ((example = request.getParameter("example")) == null)
            throw new Exception();
        wordId = Integer.parseInt(request.getParameter("wordId"));
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
        System.out.println(wordId + example + userId);
    }
}
