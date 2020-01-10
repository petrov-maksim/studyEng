package Test;

import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.account.toService.MessageAuthenticate;
import util.AddressService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TestServlet extends HttpServlet implements Abonent {
    private final static Address address = new Address();
    private String sessionId;
    private HttpServletResponse response;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("get request, id:" + req.getSession().getId());
        sessionId = req.getSession().getId();
        response = resp;
        System.out.println(resp.getHeader("handling"));

        /**
         * Проверка, является ли вызов первым, если параметр установлен - нет, в этом случае проходимся по очереди
         * и проверяем, выполнил ли сервис запрос для определенного пользователя.
         * Иначе формируем запрос.
         */
        if (req.getHeader("handling") != null) {
            System.out.println("handling == true");
            MessageSystem.INSTANCE.execForServlet(this);
        }
        else {
            System.out.println(req.getHeader("mail") + " " + req.getHeader("password"));
            MessageSystem.INSTANCE.sendMessageForService(new MessageAuthenticate(address, AddressService.INSTANCE.getAccountServiceAddress(),
                    req.getParameter("mail"), req.getParameter("password"), req.getSession().getId()));
            resp.getWriter().write("test");
        }
    }

    @Override
    public Address getAddress() {
        return address;
    }

    public String getSessionId(){
        return sessionId;
    }

    public void handled(){
        System.out.println("handled");
        response.setHeader("ready","true");
        try {
            response.getWriter().write("Finally handled");
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waiting(){

    }
}
