package servlets.dictionary.user;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MessageAddWordForUser;
import servlets.BaseServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Для первичного добавления слова пользователю (во все слова)
 */
public class AddWordForUserServlet extends HttpServlet implements BaseServlet {
    private static final Address address = new Address();
    private HttpServletResponse response;
    private String word;
    private String translation;
    private String sessionId;
    private int userId;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        response = resp;
        sessionId = req.getSession().getId();

        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        //First request
        if (req.getHeader("handling") == null) {
            if (!initParams(req)){
                System.out.println("Wrong parameters in RemoveWordServlet");
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
        MessageSystem.INSTANCE.sendMessageForService(new MessageAddWordForUser(getAdr(), AddressService.INSTANCE.getDictionaryService(),
                word, translation, userId, sessionId));
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

    private boolean initParams(HttpServletRequest request) {
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
        word = request.getParameter("word");
        translation = request.getParameter("translation");

        if (word == null || translation == null)
            return false;
        return true;
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

