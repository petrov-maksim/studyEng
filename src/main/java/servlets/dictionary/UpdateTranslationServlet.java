package servlets.dictionary;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MessageUpdateTranslation;
import servlets.BaseServlet;
import servlets.NonAbonentServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * При изменении перевода, сервлету приходят новый перевод и id старого перевода
 * Заменяем старый перевод на новый и возвращаем id нового перевода
 */
public class UpdateTranslationServlet extends HttpServlet implements NonAbonentServlet {
    private String sessionId;
    private int userId;
    private int wordId;
    private int oldTranslationId;
    private String newTranslation;

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
            System.out.println("Wrong parameters in GetWordFromWordSet");
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
        MessageSystem.INSTANCE.sendMessageForService(new MessageUpdateTranslation(null, AddressService.INSTANCE.getDictionaryService(),
                userId, wordId, oldTranslationId, newTranslation));
    }

    private void initParams(HttpServletRequest request) throws Exception {
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
        wordId = Integer.parseInt(request.getParameter("wordId"));
        oldTranslationId = Integer.parseInt(request.getParameter("translationId"));
        if ((newTranslation = request.getParameter("translation")) == null)
            throw new Exception();
    }
}
