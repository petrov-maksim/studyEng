package servlets.account;

import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет осуществляющий выход пользователя из сервиса
 */
public class SignOutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        SessionCache.INSTANCE.deauthorize(req.getSession().getId());
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
