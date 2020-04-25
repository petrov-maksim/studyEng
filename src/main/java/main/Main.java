package main;

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
import servlets.dictionary.*;
import servlets.dictionary.user.AddWordForUserServlet;
import servlets.dictionary.user.GetWordsForUserServlet;
import servlets.dictionary.wordSet.*;
import servlets.dictionary.user.RemoveWordsForUserServlet;
import servlets.grammar.AddWordsForUserServlet;
import servlets.grammar.GetCurrentLvlServlet;
import servlets.grammar.UpdateCurrentLvlServlet;
import servlets.trainings.*;
import util.QueryExecutor;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    static {
        try(FileInputStream ins = new FileInputStream("log.config")){
            LogManager.getLogManager().readConfiguration(ins);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static ServletContextHandler servletHandler;

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        initServices();
        initServlets();

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("static/");
        resourceHandler.setWelcomeFiles(new String[]{"auth/SignIn.html", "dictionary/dictionary.html"});

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, servletHandler});

        server.setHandler(handlers);

        server.start();
        server.join();
    }

    private static void initServlets(){
        servletHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletHandler.setSessionHandler(new SessionHandler());

        servletHandler.addServlet(SignUpServlet.class, "/signUp");
        servletHandler.addServlet(SignInServlet.class, "/signIn");
        servletHandler.addServlet(SignOutServlet.class, "/signOut");
        servletHandler.addServlet(IsUserAuthorisedServlet.class, "/authorised");

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

        servletHandler.addServlet(AddWordsForUserServlet.class, "/grammar/addWordsForUser");
        servletHandler.addServlet(UpdateCurrentLvlServlet.class, "/grammar/updateCurLvl");
        servletHandler.addServlet(GetCurrentLvlServlet.class, "/grammar/getCurLvl");
    }

    private static void initServices() throws SQLException, ClassNotFoundException {
        String dbDriverName = null;
        String dbUrl = null;
        String dbLogin = null;
        String dbPassword = null;

        Properties properties = new Properties();
        try(FileReader fileReader = new FileReader("DB.config")) {
            properties.load(fileReader);
            dbDriverName = properties.getProperty("driver");
            dbUrl = properties.getProperty("url");
            dbLogin = properties.getProperty("login");
            dbPassword = properties.getProperty("password");
        } catch (IOException e) {
            Logger.getLogger(Main.class.toString()).log(Level.SEVERE,"Can't load db config so goodbye", e);
            System.exit(0);
        }

        if (Objects.isNull(dbDriverName) || Objects.isNull(dbUrl) || Objects.isNull(dbLogin) || Objects.isNull(dbPassword)){
            Logger.getLogger(Main.class.toString()).log(Level.SEVERE,"Not all db configurations were loaded");
            System.exit(0);
        }

        Class.forName(dbDriverName);
        AccountService accountService = new AccountService(new QueryExecutor(DriverManager.getConnection(dbUrl, dbLogin, dbPassword)));

        DictionaryService dictionaryServiceThread1 = new DictionaryService(new QueryExecutor(DriverManager.getConnection(dbUrl, dbLogin, dbPassword)));
        DictionaryService dictionaryServiceThread2 = new DictionaryService(new QueryExecutor(DriverManager.getConnection(dbUrl, dbLogin, dbPassword)));

        TrainingService trainingServiceThread1 = new TrainingService(new QueryExecutor(DriverManager.getConnection(dbUrl, dbLogin, dbPassword)));
        TrainingService trainingServiceThread2 = new TrainingService(new QueryExecutor(DriverManager.getConnection(dbUrl, dbLogin, dbPassword)));

        GrammarService grammarServiceThread1 = new GrammarService(new QueryExecutor(DriverManager.getConnection(dbUrl, dbLogin, dbPassword)));
        GrammarService grammarServiceThread2 = new GrammarService(new QueryExecutor(DriverManager.getConnection(dbUrl, dbLogin, dbPassword)));

        new Thread(accountService).start();
        new Thread(dictionaryServiceThread1).start();
        new Thread(dictionaryServiceThread2).start();
        new Thread(trainingServiceThread1).start();
        new Thread(trainingServiceThread2).start();
        new Thread(grammarServiceThread1).start();
        new Thread(grammarServiceThread2).start();
    }
}



