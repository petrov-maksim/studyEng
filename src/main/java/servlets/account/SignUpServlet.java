package servlets.account;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.account.toService.MsgRegister;
import servlets.ServletAbonent;
import util.AddressService;
import util.SessionCache;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Сервлет осуществляющий регистрацию пользователя
 */
public class SignUpServlet extends ServletAbonent {
    private static final Address address = new Address();
    private HttpServletResponse response;
    private String sessionId;
    private String mail;
    private String password;
    private String name;

    private static final Pattern MAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

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
            }
            catch (Exception e) {
                Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "sign up data is not valid", e);
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
        MessageSystem.INSTANCE.sendMessageForService(new MsgRegister(getAddress(), AddressService.INSTANCE.getAccountServiceAddress(),
                mail, password, name, sessionId));
    }

    public void handle(boolean isRegistered){
        try {
            response.setStatus(isRegistered ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);
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
        mail = request.getHeader("mail").trim();
        name = request.getHeader("name").trim();
        password = request.getHeader("password").trim();
        validate();
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

    private void validate() throws Exception {
        if (mail == null || mail.isBlank() || name  == null ||  name.isBlank() || password == null ||  password.isBlank())
            throw new Exception("blank data");

        if(!MAIL_PATTERN.matcher(mail).matches())
            throw new Exception("Invalid mail");

        if (name.length() > 26 || password.length() > 30)
            throw new Exception("Too long name or password");
    }
}
