package servlets.dictionary.wordSet;

import messageSystem.Address;
import servlets.BaseServlet;

import javax.servlet.http.HttpServlet;

public class GetWordSetsServlet extends HttpServlet implements BaseServlet {
    private static final Address address = new Address();
    @Override
    public void createMessage() {

    }

    @Override
    public void checkServiceResult() {

    }

    @Override
    public String getSessionId() {
        return null;
    }

    @Override
    public Address getAddress() {
        return getAdr();
    }

    public Address getAdr(){return address;}
}