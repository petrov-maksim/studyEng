package servlets.content;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.content.toService.MessageGetContent;
import servlets.BaseServlet;
import util.AddressService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ContentServlet extends HttpServlet implements BaseServlet {
    private static final Address address = new Address();
    private String sessionId;
    private HttpServletResponse response;
    private HttpServletRequest request;
    private static final int N_CONTENTS = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.sessionId = request.getSession().getId();
        this.response = response;
        this.request = request;
        //First request
        if (request.getHeader("handling") == null)
            createMessage();
            //Not the first request
        else
            checkServiceResult();
    }

    public void handleRequest(String videos[], String texts[]) {
        response.setHeader("ready", "true");
        System.out.println(response);
        try {
            response.getWriter().write("Number of videos: " + videos.length + "\nNumber of texts: " + texts.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createMessage() {
        if (request.getParameter("type") == null)
            MessageSystem.INSTANCE.sendMessageForService(new MessageGetContent(getAddress(), AddressService.INSTANCE.getContentServiceAddress(),
                    N_CONTENTS / 2, N_CONTENTS / 2, sessionId));
        else if (request.getParameter("type").equals("video"))
            MessageSystem.INSTANCE.sendMessageForService(new MessageGetContent(getAddress(), AddressService.INSTANCE.getContentServiceAddress(),
                N_CONTENTS, 0, sessionId));
        else if (request.getParameter("type").equals("text"))
            MessageSystem.INSTANCE.sendMessageForService(new MessageGetContent(getAddress(), AddressService.INSTANCE.getContentServiceAddress(),
                    0, N_CONTENTS, sessionId));
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
