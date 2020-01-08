package Test;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import servlets.account.SignInServlet;
import servlets.account.SignOutServlet;
import servlets.account.SignUpServlet;
import servlets.content.ContentByIdServlet;
import servlets.content.ContentServlet;
import servlets.grammar.GrammarServlet;

public class Test {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("static/temp");
        resourceHandler.setWelcomeFiles(new String[]{"test.html"});

        ServletContextHandler servletHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletHandler.addServlet(SignInServlet.class, "/signin");
        servletHandler.addServlet(SignUpServlet.class, "/signup");
        servletHandler.addServlet(SignOutServlet.class, "/signout");
        servletHandler.addServlet(GrammarServlet.class, "/grammar/*");
        servletHandler.addServlet(ContentServlet.class, "/content");
        servletHandler.addServlet(ContentByIdServlet.class, "/content/*");

        servletHandler.setSessionHandler(new SessionHandler());

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler,servletHandler});

        server.setHandler(handlers);

        server.start();
        server.join();
    }
}

