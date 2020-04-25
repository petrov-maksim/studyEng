package servlets.dictionary.wordSet;


import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MsgAddWordsToWordSet;
import servlets.NonAbonentServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Сервлет, обрабатывающий запрос на добавление слов в набор.
 * На данный момент, word's ids лежат в заголовке:
 * wordId: 1,2,3,4,5
 */
public class AddWordsToWordSetServlet extends HttpServlet implements NonAbonentServlet {
    private HttpServletResponse response;
    private List<Integer> wordIds;
    private int wordSetId;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        response = resp;
        String sessionId = req.getSession().getId();

        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        try{
            initParams(req);
        }catch (Exception e){
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE,
                    "Wrong parameters:\nwordIds: " + wordIds
                    + "\nwordSetId:" + wordSetId, e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.flushBuffer();
            return;
        }

        createMessage();

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.flushBuffer();
    }

    @Override
    public void createMessage() {
        MessageSystem.INSTANCE.sendMessageForService(new MsgAddWordsToWordSet(null, AddressService.INSTANCE.getDictionaryServiceAddress(),
                 wordIds, wordSetId));
    }


    public void handle(int wordId){
        try{
            response.setStatus(wordId == -1 ?
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR :
                    HttpServletResponse.SC_OK);
            response.setHeader("ready", "true");
            response.setHeader("wordId", String.valueOf(wordId));
            response.flushBuffer();
        }catch (IOException e){
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
        }
    }

    private void initParams(HttpServletRequest request) {
        wordIds = Arrays.stream(request.getParameter("wordId").split(","))
                .map(Integer::parseInt).collect(Collectors.toList());
        wordSetId = Integer.parseInt(request.getParameter("wordSetId"));
    }
}
