package servlets.content;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.content.MessageGetContentById;
import servlets.BaseServlet;
import util.AddressService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ContentByIdServlet extends HttpServlet implements BaseServlet {
    private static final Address address = new Address();
    private String sessionId;
    private HttpServletResponse response;
    private String type;
    private int contentId;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.response = response;
        //First request
        if (request.getHeader("handling") == null) {
            sessionId = request.getSession().getId();
            contentId = Integer.parseInt(request.getPathInfo().substring(1).split("/")[0]);
            type = request.getParameter("type");
            createMessage();
        }
            //Not the first request
        else
            checkServiceResult();
    }

    public void handleRequest(String payload) {
        response.setHeader("ready", "true");
        System.out.println(response);
        try {
            response.getWriter().write(payload);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createMessage() {
        MessageSystem.INSTANCE.sendMessageForService(new MessageGetContentById(getAddress(), AddressService.INSTANCE.getContentServiceAddress(),
                sessionId, type, contentId));
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