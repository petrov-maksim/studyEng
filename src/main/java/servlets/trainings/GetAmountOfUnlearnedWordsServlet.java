package servlets.trainings;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Training;
import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.trainings.toService.MsgToGetAmountOfUnlearnedWords;
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
 * Сервлет, обрабатывающий запрос на получение количества неизученных слов
 */
public class GetAmountOfUnlearnedWordsServlet extends ServletAbonent {
    private static final Address address = new Address();
    private HttpServletResponse response;
    private String sessionId;
    private int userId;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        sessionId = req.getSession().getId();

        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
        response = resp;

        //First request
        if (req.getHeader("handling") == null) {
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
        MessageSystem.INSTANCE.sendMessageForService(new MsgToGetAmountOfUnlearnedWords(getAddress(), AddressService.INSTANCE.getTrainingServiceAddress(),
                userId, sessionId));
    }

    public void handle(Collection<Training> trainings){
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(READY, "true");

        try{
            if (trainings == null || trainings.size() == 0)
                response.flushBuffer();
            else {
                ObjectMapper objectMapper = new ObjectMapper();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().write(objectMapper.writeValueAsString(trainings));
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
}