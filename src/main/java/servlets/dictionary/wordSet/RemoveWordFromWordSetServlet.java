package servlets.dictionary.wordSet;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MessageRemoveWordsForUser;
import messageSystem.messages.dictionary.toService.MessageRemoveWordsFromWordSet;
import servlets.BaseServlet;
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
 * На фронтенде, если слов больше, чем пороговое значение, то разбить вызовы на сервер, в несколько заходов
 * На данный момент, word's ids лежат в заголовке:
 * wordId: 1,2,3,4,5
 * Если wordSetId не был передан, удаляем для user'a, иначе только из wordSet'a
 */

public class RemoveWordFromWordSetServlet extends HttpServlet implements NonAbonentServlet {
    private String sessionId;
    private Integer wordIds[];
    private int wordSetId = -1;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sessionId = req.getSession().getId();

        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        //First request
        if (req.getHeader("handling") == null) {
            try{
                initParams(req);
            }catch (Exception e){
                //logging
                System.out.println("Wrong parameters in RemoveWordFromWordSetServlet");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.flushBuffer();
                return;
            }
            createMessage();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.flushBuffer();
        }
    }

    @Override
    public void createMessage() {
        MessageSystem.INSTANCE.sendMessageForService(new MessageRemoveWordsFromWordSet(null, AddressService.INSTANCE.getDictionaryService(),
                wordIds,wordSetId, sessionId));
    }



    private void initParams(HttpServletRequest request){
        wordSetId = Integer.parseInt(request.getParameter("wordSetId"));
        wordIds = Arrays.stream(request.getParameter("wordId").split(",")).
                map(Integer::parseInt).toArray(Integer[]::new);
    }

}
