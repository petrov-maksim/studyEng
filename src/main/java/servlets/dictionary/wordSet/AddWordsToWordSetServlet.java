package servlets.dictionary.wordSet;


import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MessageAddWordsToWordSet;
import servlets.NonAbonentServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Не возвращает ничего
 * На фронтенде, если слов больше, чем пороговое значение, то разбить вызовы на сервер, в несколько заходов
 * На данный момент, word's ids лежат в заголовке:
 * wordId: 1,2,3,4,5
 */
public class AddWordsToWordSetServlet extends HttpServlet implements NonAbonentServlet {
    private HttpServletResponse response;
    private String sessionId;
    private List<Integer> wordIds;
    private int wordSetId;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        response = resp;
        sessionId = req.getSession().getId();

        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        try{
            initParams(req);
        }catch (Exception e){
            System.out.println("Wrong parameters in AddWordsToWordSetServlet");
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
        MessageSystem.INSTANCE.sendMessageForService(new MessageAddWordsToWordSet(null, AddressService.INSTANCE.getDictionaryServiceAddress(),
                 wordIds, wordSetId));
    }


    public void handle(int wordId){
        int sc = wordId == -1 ? HttpServletResponse.SC_INTERNAL_SERVER_ERROR : HttpServletResponse.SC_OK;
        try{
            response.setStatus(sc);
            response.setHeader("ready", "true");
            response.setHeader("wordId", String.valueOf(wordId));
            response.flushBuffer();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void initParams(HttpServletRequest request) {
        wordIds = Arrays.stream(request.getParameter("wordId").split(","))
                .map(Integer::parseInt).collect(Collectors.toList());
        wordSetId = Integer.parseInt(request.getParameter("wordSetId"));
    }
}
