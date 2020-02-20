package servlets.content;

import entities.content.ContentTypes;
import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.content.toService.MessageGetContentById;
import servlets.BaseServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetContentByIdServlet extends HttpServlet implements BaseServlet {
    private static final Address address = new Address();
    private String sessionId;
    private HttpServletResponse response;
    private ContentTypes type;
    private int contentId;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.response = response;
        this.sessionId = request.getSession().getId();
        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.flushBuffer();
            return;
        }

        //First request
        if (request.getHeader("handling") == null) {
            try{
                initParams(request);
            }catch (Exception e){
                System.out.println("Wrong parameters in GetContentByIdServlet");
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.flushBuffer();
                return;
            }

            createMessage();
            response.setStatus(HttpServletResponse.SC_OK);
            response.flushBuffer();
        }
        //Not the first request
        else
            checkServiceResult();
    }

    public void handleRequest() {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createMessage() {
        MessageSystem.INSTANCE.sendMessageForService(new MessageGetContentById(getAddress(), AddressService.INSTANCE.getContentServiceAddress(),
                sessionId, type, contentId));
    }

    private void initParams(HttpServletRequest request) throws Exception {
        contentId = Integer.parseInt(request.getParameter("id"));
        type = ContentTypes.getType(request.getParameter("type"));
    }

    @Override
    public void checkServiceResult() {
        MessageSystem.INSTANCE.execForServlet(this);
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