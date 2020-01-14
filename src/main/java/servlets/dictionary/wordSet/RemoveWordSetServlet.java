package servlets.dictionary.wordSet;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MessageRemoveWordSet;
import servlets.BaseServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RemoveWordSetServlet extends HttpServlet implements BaseServlet {
    private static final Address address = new Address();
    private HttpServletResponse response;
    private String sessionId;
    private int userId;
    private int wordSetId;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sessionId = req.getSession().getId();
        response = resp;

        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        //First request
        if (req.getHeader("handling") == null) {
            try{
                initParams(req);
            }catch(Exception e){
                System.out.println("Wrong parameters in AddWordToWordSetServlet");
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
        MessageSystem.INSTANCE.sendMessageForService(new MessageRemoveWordSet(getAdr(), AddressService.INSTANCE.getDictionaryService(),
                userId, wordSetId, sessionId));
    }

    public void handle(boolean status){
        int sc = status ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

        try {
            response.setStatus(sc);
            response.getWriter().write("Anyway handled");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initParams(HttpServletRequest request){
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
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
