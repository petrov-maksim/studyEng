package servlets.trainings;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Word;
import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.trainings.toService.MsgToGetUnlearnedWords;
import servlets.ServletAbonent;
import util.AddressService;
import util.SessionCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервлет, обрабатывающий запрос на получение неизученных слов, для конкретной тренировки
 */
public class GetUnlearnedWordsServlet extends ServletAbonent {
    private static final Address address = new Address();
    private String sessionId;
    private int userId;
    private int trainingId;
    private HttpServletResponse response;

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
            }catch (RuntimeException e){
                Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Wrong trainingId: " + trainingId, e);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.flushBuffer();
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
        MessageSystem.INSTANCE.sendMessageForService(new MsgToGetUnlearnedWords(getAddress(),
                AddressService.INSTANCE.getTrainingServiceAddress(),
                sessionId, trainingId, userId));
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

    private void initParams(HttpServletRequest request) {
        trainingId = Integer.parseInt(request.getParameter("trainingId"));
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
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
