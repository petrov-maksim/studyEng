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
import servlets.dictionary.user.AddWordsForUserServlet;
import servlets.dictionary.wordSet.AddWordsToWordSetServlet;
import servlets.dictionary.user.GetWordForUserServlet;
import servlets.dictionary.user.RemoveWordForUserServlet;
import servlets.grammar.GrammarServlet;

public class Test {
    private static ServletContextHandler servletHandler;
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        initServlets();

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("static/temp");
        resourceHandler.setWelcomeFiles(new String[]{"test.html"});

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler,servletHandler});

        server.setHandler(handlers);

        server.start();
        server.join();
    }

    private static void initServlets(){
        servletHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletHandler.addServlet(SignInServlet.class, "/signin");
        servletHandler.addServlet(SignUpServlet.class, "/signup");
        servletHandler.addServlet(SignOutServlet.class, "/signout");
        servletHandler.addServlet(GrammarServlet.class, "/grammar/*");
        servletHandler.addServlet(ContentServlet.class, "/content");
        servletHandler.addServlet(ContentByIdServlet.class, "/content/*");
        servletHandler.addServlet(GetWordForUserServlet.class, "/dictionary/getword");
        servletHandler.addServlet(RemoveWordForUserServlet.class, "/dictionary/removeword");
        servletHandler.addServlet(AddWordsForUserServlet.class, "/dictionary/addwordforuser");
        servletHandler.addServlet(AddWordsToWordSetServlet.class, "/dictionary/addwordtowordset");

        servletHandler.setSessionHandler(new SessionHandler());
    }
}

