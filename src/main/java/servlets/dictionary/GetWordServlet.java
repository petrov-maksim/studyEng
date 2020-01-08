package servlets.dictionary;

import messageSystem.Address;
import servlets.BaseServlet;

import javax.servlet.http.HttpServlet;

public class GetWordServlet extends HttpServlet implements BaseServlet {
    private static final Address address = new Address();
    /**
     * максимальное количество слов в response
     */
    private static final int N_WORDS = 30;
    /**
     * С какого индекса необходимо формировать слова для response
     * Соответствует заголовку request'a
     */
    private int index;
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
