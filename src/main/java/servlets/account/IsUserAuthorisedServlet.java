package servlets.account;

import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет осуществляющий проверку - авторизован ли пользователь
 */
public class IsUserAuthorisedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setHeader("auth", String.valueOf(SessionCache.INSTANCE.isAuthorized(req.getSession().getId())));
        resp.flushBuffer();
    }
}
