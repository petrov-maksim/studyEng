package servlets.account;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.account.toService.MessageRegister;
import servlets.BaseServlet;
import util.AddressService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/signup"})
public class SignUpServlet extends HttpServlet implements BaseServlet {
    private static final Address address = new Address();
    private HttpServletResponse response;
    private String sessionId;
    private String mail;
    private String password;
    private String name;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        response = resp;
        sessionId = req.getSession().getId();
        mail = req.getParameter("mail");
        password = req.getParameter("password");
        name = req.getParameter("name");

        if(req.getHeader("handling") == null)
            createMessage();
        else
            checkServiceResult();
    }

    @Override
    public void createMessage() {
        MessageSystem.INSTANCE.sendMessageForService(new MessageRegister(getAddress(), AddressService.INSTANCE.getAccountServiceAddress(),
                mail, password, name, sessionId));
    }

    @Override
    public void checkServiceResult() {
        MessageSystem.INSTANCE.execForServlet(this);
    }

    public void userNotRegistered(){
        response.setHeader("ready","true");
        try {
            response.getWriter().write("Can't register");
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void userRegistered(){
        response.setHeader("ready","true");
        try {
            response.getWriter().write("registered");
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
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
