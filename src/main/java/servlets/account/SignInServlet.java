package servlets.account;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.account.toService.MessageAuthenticate;
import servlets.BaseServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/signin"})
public class SignInServlet extends HttpServlet implements BaseServlet {
    private static final Address address = new Address();
    private HttpServletResponse response;
    private String mail;
    private String password;
    private String sessionId;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        response = resp;
        sessionId = req.getSession().getId();
        mail = req.getParameter("mail");
        password = req.getParameter("password");

        if (SessionCache.INSTANCE.isAuthorized(sessionId))
            userAuthorized();
        //First request
        if (req.getHeader("handling") == null)
            createMessage();
        //Not the first request
        else
            checkServiceResult();
    }

    @Override
    public void createMessage() {
        MessageSystem.INSTANCE.sendMessageForService(new MessageAuthenticate(this.getAddress(), AddressService.INSTANCE.getAccountServiceAddress(),
                mail, password, sessionId));
        try {
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void userAuthorized(){
        response.setHeader("ready","true");
        try {
            response.getWriter().write("Congo u're authorized");
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void userNotAuthorized(){
        response.setHeader("ready","true");
        try {
            response.getWriter().write("Unauthorized testing");
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkServiceResult() {
        MessageSystem.INSTANCE.execForServlet(this);
    }

    @Override
    public Address getAddress() {
        return getAdr();
    }

    public static Address getAdr(){return address;}

    @Override
    public String getSessionId() {
        return sessionId;
    }
}
