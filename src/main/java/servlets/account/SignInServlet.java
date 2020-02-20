package servlets.account;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.account.toService.MessageAuthenticate;
import servlets.BaseServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TODO: Добавить проверку передаваемых данных на валидность:
 */
public class SignInServlet extends HttpServlet implements BaseServlet {
    private static final Address address = new Address();
    private HttpServletResponse response;
    private String mail;
    private String password;
    private String sessionId;
    private static int index = 0;


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sessionId = req.getSession().getId();
        response = resp;

        //First request
        if (req.getHeader("handling") == null) {
            if (SessionCache.INSTANCE.isAuthorized(sessionId)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setHeader(ALREADY_AUTHORIZED, "true");
                resp.flushBuffer();
                return;
            }

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
        MessageSystem.INSTANCE.sendMessageForService(new MessageAuthenticate(getAdr(), AddressService.INSTANCE.getAccountServiceAddress(),
                mail, password, sessionId));
    }


    public void handle(boolean isAuthorized){
        int sc = isAuthorized? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST;
        try {
            response.setStatus(sc);
            response.setHeader(READY, "true");
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notReady() {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader(READY, "false");
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initParams(HttpServletRequest request) throws Exception {
        mail = request.getHeader("mail");
        password = request.getHeader("password");

        if (mail == null || mail.isBlank() || password == null || password.isBlank())
            throw new Exception();
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
