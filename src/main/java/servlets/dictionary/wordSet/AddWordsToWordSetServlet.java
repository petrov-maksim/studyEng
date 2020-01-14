package servlets.dictionary.wordSet;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MessageAddWordToWordSet;
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
 * Не возвращает ничего
 * На фронтенде, если слов больше, чем пороговое значение, то разбить вызовы на сервер, в несколько заходов
 * На данный момент, word's ids лежат в заголовке:
 * wordId: 1,2,3,4,5
 */
public class AddWordsToWordSetServlet extends HttpServlet implements BaseServlet {
    private static final Address address = new Address();
    private HttpServletResponse response;
    private String sessionId;
    private int userId;
    private Integer wordIds[];
    private int wordSetId;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        response = resp;
        sessionId = req.getSession().getId();

        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        try{
            initParams(req);
        }catch (Exception e){
            System.out.println("Wrong parameters in AddWordToWordSetServlet");
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
        MessageSystem.INSTANCE.sendMessageForService(new MessageAddWordToWordSet(getAdr(), AddressService.INSTANCE.getDictionaryService(),
                 userId, wordIds, wordSetId));
    }


    public void handle(int wordId){
        int sc = wordId == -1 ? HttpServletResponse.SC_INTERNAL_SERVER_ERROR : HttpServletResponse.SC_OK;
        try{
            response.setStatus(sc);
            response.setHeader("ready", "true");
            response.setHeader("wordId", String.valueOf(wordId));
            response.flushBuffer();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void initParams(HttpServletRequest request) {
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
        wordIds = Arrays.stream(request.getParameter("wordId").split(",")).
                map(Integer::parseInt).toArray(Integer[]::new);
        wordSetId = Integer.parseInt(request.getParameter("wordSetId"));
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
