package servlets.dictionary.wordSet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Word;
import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MsgGetWordsFromWordSet;
import servlets.ServletAbonent;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервлет, обрабатывающий запрос на получение слов из набора
 */
public class GetWordsFromWordSetServlet extends ServletAbonent {
    private static final Address address = new Address();
    //максимальное количество слов в response
    private static final int N_WORDS = 10;
    private String sessionId;
    private HttpServletResponse response;
    //С какого индекса необходимо формировать слова для response, соответствует заголовку request'a
    private int index;
    private int wordSetId;
    private int userId;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
                Logger.getLogger(this.getClass().toString()).log(Level.SEVERE,
                        "Wrong parameters\n index: " + index +
                        "\nwordSetId: " + wordSetId, e);
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
        MessageSystem.INSTANCE.sendMessageForService(new MsgGetWordsFromWordSet(getAddress(), AddressService.INSTANCE.getDictionaryServiceAddress(),
                userId, wordSetId, index, N_WORDS, sessionId));
    }


    public void handle(Collection<Word> words) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(READY, "true");

        try{
            if (words == null || words.isEmpty())
                response.flushBuffer();
            else {
                ObjectMapper objectMapper = new ObjectMapper();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().write(objectMapper.writeValueAsString(words));
            }
        }catch (Exception e){
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
        }
    }

    @Override
    public void notReady() {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader(READY, "false");
            response.flushBuffer();
        } catch (IOException e) {
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
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
        index = request.getParameter("index") != null ?
                Integer.parseInt(request.getParameter("index")) :
                0;
        wordSetId = Integer.parseInt(request.getParameter("wordSetId"));
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
    }
}
