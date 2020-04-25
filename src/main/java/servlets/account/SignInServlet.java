package servlets.account;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.account.toService.MsgAuthenticate;
import servlets.ServletAbonent;
import util.AddressService;
import util.SessionCache;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервлет осуществляющий авторизацию пользователя
 */
public class SignInServlet extends ServletAbonent {
    private static final Address address = new Address();
    private HttpServletResponse response;
    private String mail;
    private String password;
    private String sessionId;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        sessionId = req.getSession().getId();
        response = resp;

        //First request
        if (req.getHeader("handling") == null) {
            if (SessionCache.INSTANCE.isAuthorized(sessionId)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.setHeader("authorized", "true");
                resp.flushBuffer();
                return;
            }

            try {
                initParams(req);
            } catch (Exception e) {
                Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Empty mail or password", e);
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
        MessageSystem.INSTANCE.sendMessageForService(new MsgAuthenticate(getAdr(), AddressService.INSTANCE.getAccountServiceAddress(),
                mail, password, sessionId));
    }


    public void handle(boolean isAuthorized){
        try {
            response.setStatus(isAuthorized ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
            response.setHeader(READY, "true");
            response.flushBuffer();
        } catch (IOException e) {
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
