package servlets.dictionary.wordSet;

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

public class GetWordFromWordSet extends HttpServlet implements BaseServlet {
    private static final Address address = new Address();
    private static final int N_WORDS = 30; //максимальное количество слов в response
    private String sessionId;
    private HttpServletResponse response;
    private int index;                      //С какого индекса необходимо формировать слова для response, соответствует заголовку request'a
    private int wordSetId;                  // Из какого набора требуются слова, соответствует заголовку request'a

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
                System.out.println("Wrong parameters in GetWordFromWordSet");
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
        MessageSystem.INSTANCE.sendMessageForService(new MessageGetWordsFromWordSet(getAddress(), AddressService.INSTANCE.getDictionaryService(),
                wordSetId, index, N_WORDS, sessionId));
    }

    @Override
    public void checkServiceResult() {
        MessageSystem.INSTANCE.execForServlet(this);
    }

    public void handle(Word[] words) {
        response.setHeader("ready", "true");
        try {
            response.getWriter().write("All done");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            wordSetId = Integer.parseInt(request.getHeader("wordSetId"));
        }catch (Exception e){
            // Logging
            e.printStackTrace();
        }
    }
}
