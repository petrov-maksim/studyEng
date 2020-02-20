package servlets.dictionary.wordSet;

import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MessageRemoveWordSet;
import servlets.NonAbonentServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RemoveWordSetServlet extends HttpServlet implements NonAbonentServlet {
    private String sessionId;
    private int wordSetId;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sessionId = req.getSession().getId();

        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        //First request
        if (req.getHeader("handling") == null) {
            try {
                initParams(req);
            } catch (Exception e) {
                System.out.println("Wrong parameters in AddWordToWordSetServlet");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.flushBuffer();
                return;
            }
            createMessage();

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.flushBuffer();
        }
    }

    @Override
    public void createMessage() {
        MessageSystem.INSTANCE.sendMessageForService(new MessageRemoveWordSet(null, AddressService.INSTANCE.getDictionaryServiceAddress(), wordSetId));
    }


    private void initParams(HttpServletRequest request) {
        wordSetId = Integer.parseInt(request.getHeader("wordSetId"));
    }
}
