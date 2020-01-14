package servlets.dictionary.wordSet;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MessageUpdateWordSet;
import servlets.BaseServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateWordSetServlet extends HttpServlet implements BaseServlet {
    private static final Address address = new Address();
    private HttpServletResponse response;
    private String sessionId;
    private String newName;
    private int userId;
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

        //First request
        if (req.getHeader("handling") == null) {
            try {
                initParams(req);
            } catch (Exception e) {
                System.out.println("Wrong parameters in UpdateWordSetServlet");
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
        MessageSystem.INSTANCE.sendMessageForService(new MessageUpdateWordSet(getAdr(), AddressService.INSTANCE.getDictionaryService(),
                newName, sessionId, userId, wordSetId));
    }

    public void handle(boolean status){
        int sc = status ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        try {
            response.setStatus(sc);
            response.getWriter().write("Handled anyway");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initParams(HttpServletRequest request) throws Exception {
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
        wordSetId = Integer.parseInt(request.getParameter("wordSetId"));
        newName = request.getParameter("name");
        System.out.println(newName);
        if (newName == null)
            throw new Exception();
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
