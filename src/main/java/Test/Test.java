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
import servlets.dictionary.*;
import servlets.dictionary.user.AddWordForUserServlet;
import servlets.dictionary.wordSet.*;
import servlets.dictionary.user.RemoveWordsForUserServlet;
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

        servletHandler.addServlet(SignUpServlet.class, "/signUp");
        servletHandler.addServlet(SignInServlet.class, "/signIn");
        servletHandler.addServlet(SignOutServlet.class, "/signOut");

        servletHandler.addServlet(GrammarServlet.class, "/grammar/*");

        servletHandler.addServlet(ContentServlet.class, "/content");
        servletHandler.addServlet(ContentByIdServlet.class, "/content/*");

        servletHandler.addServlet(GetWordsFromWordSetServlet.class, "/dictionary/getWordsForUser");
        servletHandler.addServlet(GetWordsFromWordSetServlet.class, "/dictionary/getWordsFromWordSet");

        servletHandler.addServlet(RemoveWordsForUserServlet.class, "/dictionary/removewordforuser");
        servletHandler.addServlet(RemoveWordFromWordSetServlet.class, "/dictionary/removewordfromwordset");

        servletHandler.addServlet(AddWordForUserServlet.class, "/dictionary/addwordforuser");
        servletHandler.addServlet(AddWordsToWordSetServlet.class, "/dictionary/addwordtowordset");

        servletHandler.addServlet(GetWordSetsServlet.class, "/dictionary/getwordsets");
        servletHandler.addServlet(RemoveWordSetServlet.class, "/dictionary/removewordset");
        servletHandler.addServlet(UpdateWordSetServlet.class, "/dictionary/updatewordset");
        servletHandler.addServlet(AddWordSetServlet.class, "/dictionary/addWordSet");

        servletHandler.addServlet(AddTranslationServlet.class, "/dictionary/addTranslation");
        servletHandler.addServlet(RemoveTranslationServlet.class, "/dictionary/removeTranslation");
        servletHandler.addServlet(GetTranslationsForWordServlet.class, "/dictionary/getTranslationsForWord");

        servletHandler.addServlet(AddExampleServlet.class, "/dictionary/addExample");

        servletHandler.setSessionHandler(new SessionHandler());
    }
}

