package servlets.dictionary.user;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MessageRemoveWordsForUser;
import messageSystem.messages.dictionary.toService.MessageRemoveWordsFromWordSet;
import servlets.BaseServlet;
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

public class RemoveWordForUserServlet extends HttpServlet implements BaseServlet {
    private static final Address address = new Address();
    private HttpServletResponse response;
    private String sessionId;
    private Integer wordIds[];
    private int userId;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        response = resp;
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
                System.out.println("Wrong parameters in RemoveWordForUserServlet");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.flushBuffer();
                return;
            }
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
        MessageSystem.INSTANCE.sendMessageForService(new MessageRemoveWordsForUser(getAdr(), AddressService.INSTANCE.getDictionaryService(),
                wordIds, userId, sessionId));
    }

    @Override
    public void checkServiceResult() {
        MessageSystem.INSTANCE.execForServlet(this);
    }

    public void handle(boolean status){
        int sc = status ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        try{
            response.setStatus(sc);
            response.setHeader("ready", "true");
            response.flushBuffer();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void initParams(HttpServletRequest request){
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
        wordIds = Arrays.stream(request.getParameter("wordId").split(",")).
                map(Integer::parseInt).toArray(Integer[]::new);
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
