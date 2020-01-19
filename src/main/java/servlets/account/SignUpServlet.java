package servlets.account;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.account.toService.MessageRegister;
import servlets.BaseServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TODO: Добавить проверку передаваемых данных на валидность
 * Длина имени, не >= 26
 * regex на email
 */
public class SignUpServlet extends HttpServlet implements BaseServlet {
    private static final Address address = new Address();
    private HttpServletResponse response;
    private String sessionId;
    private String mail;
    private String password;
    private String name;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        sessionId = req.getSession().getId();
        response = resp;

        //First request
        if (req.getHeader("handling") == null) {
            try {
                initParams(req);
            } catch (Exception e) {
                System.out.println("Wrong parameters in SignInServlet");
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
        MessageSystem.INSTANCE.sendMessageForService(new MessageRegister(getAddress(), AddressService.INSTANCE.getAccountServiceAddress(),
                mail, password, name, sessionId));
    }

    public void handle(boolean isRegistered){
        response.setHeader("ready","true");
        try {
            response.getWriter().write("U're registered: " + isRegistered);
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initParams(HttpServletRequest request){
        mail = request.getParameter("mail");
        password = request.getParameter("password");
        name = request.getParameter("name");
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
