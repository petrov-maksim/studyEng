package servlets.dictionary.user;

import entities.Word;
import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MessageGetWordsForUser;
import messageSystem.messages.dictionary.toService.MessageGetWordsFromWordSet;
import servlets.BaseServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetWordsForUserServlet extends HttpServlet implements BaseServlet {
    private static final Address address = new Address();
    private static final int N_WORDS = 30; //максимальное количество слов в response
    private String sessionId;
    private int userId;
    private HttpServletResponse response;
    private int index;                      //С какого индекса необходимо формировать слова для response, соответствует заголовку request'a

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
                System.out.println("Wrong parameters in GetWordForUser");
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
        MessageSystem.INSTANCE.sendMessageForService(new MessageGetWordsForUser(getAddress(), AddressService.INSTANCE.getDictionaryService(),
                sessionId, userId, index, N_WORDS));
    }

    public void handle(Word[] words) {
        response.setHeader("ready", "true");
        try {
            response.getWriter().write("All done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void userNotAuthorized() throws IOException {
        response.setHeader("ready", "true");
        response.getWriter().write("U are not authorized");
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

    private void initParams(HttpServletRequest request){
        try{
            index = Integer.parseInt(request.getHeader("index"));
            userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
        }catch (Exception e){
            // Logging
            System.out.println("wrong index in GetWordForUserServlet");
        }
    }
}
