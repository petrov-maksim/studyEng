package servlets.dictionary.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Word;
import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MsgAddWordForUser;
import servlets.ServletAbonent;
import util.AddressService;
import util.SessionCache;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервлет обрабатывающий запрос на добавление слова.
 */
public class AddWordForUserServlet extends ServletAbonent {
    private static final Address address = new Address();
    private HttpServletResponse response;
    private String word;
    private String translation;
    private String sessionId;
    private int userId;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
                Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
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
        MessageSystem.INSTANCE.sendMessageForService(new MsgAddWordForUser(getAdr(), AddressService.INSTANCE.getDictionaryServiceAddress(),
                word, translation, userId, sessionId));
    }

    public void handle(int wordId){
        int sc =
                wordId == -1 ? HttpServletResponse.SC_INTERNAL_SERVER_ERROR :
                wordId == 0 ? HttpServletResponse.SC_BAD_REQUEST :
                        HttpServletResponse.SC_OK;
        try{
            response.setStatus(sc);
            response.setHeader(READY, "true");
            if (wordId > 0)
                response.getWriter().write(String.valueOf(wordId));
            response.flushBuffer();
        }catch (IOException e){
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

    private void initParams(HttpServletRequest request) throws Exception {
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
        ObjectMapper mapper = new ObjectMapper();
        Word obj = mapper.readValue(request.getReader().readLine(), Word.class);
        word = obj.getWord();
        translation = obj.getTranslations().get(0);

        if (word == null || word.isBlank() || translation == null || translation.isBlank())
            throw new Exception("word or translation is blank");
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

