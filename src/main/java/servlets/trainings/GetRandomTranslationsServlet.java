package servlets.trainings;

import com.fasterxml.jackson.databind.ObjectMapper;
import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.trainings.toService.MsgToGetRandomTranslations;
import servlets.ServletAbonent;
import util.AddressService;
import util.SessionCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервлет, обрабатывающий запрос на получение случайных переводов
 */
public class GetRandomTranslationsServlet extends ServletAbonent {
    private static final Address address = new Address();
    private HttpServletResponse response;
    private String sessionId;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        sessionId = req.getSession().getId();
        response = resp;

        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

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
        MessageSystem.INSTANCE.sendMessageForService(new MsgToGetRandomTranslations(getAddress(),
                AddressService.INSTANCE.getTrainingServiceAddress(),
                sessionId));
    }

    public void handle(List<String> translations){
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(READY, "true");

        try{
            if (translations == null || translations.isEmpty())
                response.flushBuffer();
            else {
                ObjectMapper objectMapper = new ObjectMapper();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().write(objectMapper.writeValueAsString(translations));
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

    public static Address getAdr(){
        return address;
    }
}
