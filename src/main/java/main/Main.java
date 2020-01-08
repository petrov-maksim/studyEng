package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import servlets.account.SignInServlet;
import servlets.account.SignUpServlet;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(SignInServlet.class, "/signin");
        servletContextHandler.addServlet(SignUpServlet.class, "/signup");

        server.setHandler(servletContextHandler);

        server.start();
        server.join();
    }
}
