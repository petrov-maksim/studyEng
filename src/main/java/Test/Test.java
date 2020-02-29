package Test;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import services.db.*;
import servlets.account.IsUserAuthorisedServlet;
import servlets.account.SignInServlet;
import servlets.account.SignOutServlet;
import servlets.account.SignUpServlet;
import servlets.content.GetContentByIdServlet;
import servlets.content.GetAllContentVideosServlet;
import servlets.dictionary.*;
import servlets.dictionary.user.AddWordForUserServlet;
import servlets.dictionary.user.GetWordsForUserServlet;
import servlets.dictionary.wordSet.*;
import servlets.dictionary.user.RemoveWordsForUserServlet;
import servlets.grammar.GrammarServlet;
import servlets.trainings.*;
import util.QueryExecutor;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Test {
    private static ServletContextHandler servletHandler;
    private static AccountService accountService;

    private static DictionaryService dictionaryServiceThread1;
    private static DictionaryService dictionaryServiceThread2;
    private static DictionaryService dictionaryServiceThread3;

    private static TrainingService trainingServiceThread1;
    private static TrainingService trainingServiceThread2;
    private static TrainingService trainingServiceThread3;

    private static ContentService contentServiceThread1;
    private static ContentService contentServiceThread2;
    private static ContentService contentServiceThread3;

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        initServices();
        initServlets();

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("static/");
        resourceHandler.setWelcomeFiles(new String[]{"auth/SignIn.html", "dictionary/dictionary.html"});

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
        servletHandler.addServlet(IsUserAuthorisedServlet.class, "/authorised");

        servletHandler.addServlet(GrammarServlet.class, "/grammar/*");

        servletHandler.addServlet(GetAllContentVideosServlet.class, "/content/getAllContentVideos");
        servletHandler.addServlet(GetContentByIdServlet.class, "/content/*");

        servletHandler.addServlet(GetWordsForUserServlet.class, "/dictionary/getWordsForUser");
        servletHandler.addServlet(GetWordsFromWordSetServlet.class, "/dictionary/getWordsFromWordSet");

        servletHandler.addServlet(RemoveWordsForUserServlet.class, "/dictionary/removeWordForUser");
        servletHandler.addServlet(RemoveWordsFromWordSetServlet.class, "/dictionary/removeWordsFromWordSet");

        servletHandler.addServlet(AddWordForUserServlet.class, "/dictionary/addWordForUser");
        servletHandler.addServlet(AddWordsToWordSetServlet.class, "/dictionary/addWordsToWordSet");

        servletHandler.addServlet(GetWordSetsServlet.class, "/dictionary/getWordSets");
        servletHandler.addServlet(RemoveWordSetServlet.class, "/dictionary/removeWordSet");
        servletHandler.addServlet(UpdateWordSetServlet.class, "/dictionary/updateWordSet");
        servletHandler.addServlet(AddWordSetServlet.class, "/dictionary/addWordSet");

        servletHandler.addServlet(AddTranslationServlet.class, "/dictionary/addTranslation");
        servletHandler.addServlet(RemoveTranslationServlet.class, "/dictionary/removeTranslation");

        servletHandler.addServlet(AddExampleServlet.class, "/dictionary/addExample");

        servletHandler.addServlet(GetAmountOfUnlearnedWordsServlet.class, "/trainings/getAmountOfUnlearnedWords");
        servletHandler.addServlet(GetRandomTranslationsServlet.class, "/trainings/getRandomTranslations");
        servletHandler.addServlet(GetRandomWordsServlet.class, "/trainings/getRandomWords");
        servletHandler.addServlet(GetUnlearnedWordsServlet.class, "/trainings/getUnlearnedWords");
        servletHandler.addServlet(MoveWordsToLearnedServlet.class, "/trainings/moveWordsToLearned");
        servletHandler.addServlet(MoveWordsToLearningServlet.class, "/trainings/moveWordsToLearning");

        servletHandler.setSessionHandler(new SessionHandler());
    }

    private static void initServices() throws SQLException, ClassNotFoundException {
        Class.forName(DBConfig.DB_DRIVER.getValue());
        accountService = new AccountService(new QueryExecutor(DriverManager.getConnection(DBConfig.DB_URL.getValue(), DBConfig.DB_LOGIN.getValue(), DBConfig.DB_PASSWORD.getValue())));

        dictionaryServiceThread1 = new DictionaryService(new QueryExecutor(DriverManager.getConnection(DBConfig.DB_URL.getValue(), DBConfig.DB_LOGIN.getValue(), DBConfig.DB_PASSWORD.getValue())));
        dictionaryServiceThread2 = new DictionaryService(new QueryExecutor(DriverManager.getConnection(DBConfig.DB_URL.getValue(), DBConfig.DB_LOGIN.getValue(), DBConfig.DB_PASSWORD.getValue())));
        dictionaryServiceThread3 = new DictionaryService(new QueryExecutor(DriverManager.getConnection(DBConfig.DB_URL.getValue(), DBConfig.DB_LOGIN.getValue(), DBConfig.DB_PASSWORD.getValue())));

        trainingServiceThread1 = new TrainingService(new QueryExecutor(DriverManager.getConnection(DBConfig.DB_URL.getValue(), DBConfig.DB_LOGIN.getValue(), DBConfig.DB_PASSWORD.getValue())));
        trainingServiceThread2 = new TrainingService(new QueryExecutor(DriverManager.getConnection(DBConfig.DB_URL.getValue(), DBConfig.DB_LOGIN.getValue(), DBConfig.DB_PASSWORD.getValue())));
        trainingServiceThread3 = new TrainingService(new QueryExecutor(DriverManager.getConnection(DBConfig.DB_URL.getValue(), DBConfig.DB_LOGIN.getValue(), DBConfig.DB_PASSWORD.getValue())));

//        contentServiceThread1 = new ContentService(new QueryExecutor(DriverManager.getConnection(DBConfig.DB_URL.getValue(), DBConfig.DB_LOGIN.getValue(), DBConfig.DB_PASSWORD.getValue())));
//        contentServiceThread2 = new ContentService(new QueryExecutor(DriverManager.getConnection(DBConfig.DB_URL.getValue(), DBConfig.DB_LOGIN.getValue(), DBConfig.DB_PASSWORD.getValue())));
//        contentServiceThread3 = new ContentService(new QueryExecutor(DriverManager.getConnection(DBConfig.DB_URL.getValue(), DBConfig.DB_LOGIN.getValue(), DBConfig.DB_PASSWORD.getValue())));

        new Thread(accountService).start();

        new Thread(dictionaryServiceThread1).start();
        new Thread(dictionaryServiceThread2).start();
        new Thread(dictionaryServiceThread3).start();

        new Thread(trainingServiceThread1).start();
        new Thread(trainingServiceThread2).start();
        new Thread(trainingServiceThread3).start();

        new Thread(contentServiceThread1).start();
        new Thread(contentServiceThread2).start();
        new Thread(contentServiceThread3).start();
    }
}

