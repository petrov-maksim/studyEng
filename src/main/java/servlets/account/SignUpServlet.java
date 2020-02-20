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
import java.util.regex.Pattern;

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

//    private static final Pattern MAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
            }
            catch (Exception e) {
                System.out.println("Wrong parameters in SignUpServlet"  + e);
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
        int sc = isRegistered ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST;
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
        mail = request.getHeader("mail").trim();
        name = request.getHeader("name").trim();
        password = request.getHeader("password").trim();
//        validate();
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

//    private void validate() throws Exception {
//        if (mail == null || mail.isBlank() || name  == null ||  name.isBlank() || password == null ||  password.isBlank())
//            throw new Exception("Smt empty");
//
//        if(!MAIL_PATTERN.matcher(mail).matches())
//            throw new Exception("Invalid mail");
//        if (name.length() > 26 || password.length() > 30)
//            throw new Exception("Too long name or password");
//    }
}
