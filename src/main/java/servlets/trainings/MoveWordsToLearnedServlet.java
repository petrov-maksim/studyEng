package servlets.trainings;

import messageSystem.MessageSystem;
import messageSystem.messages.trainings.toService.MessageToMoveWordsToLearned;
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
import java.util.stream.Collectors;

public class MoveWordsToLearnedServlet extends HttpServlet implements NonAbonentServlet {
    private String sessionId;
    private int userId;
    private int trainingId;
    private List<Integer> wordIds;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sessionId = req.getSession().getId();

        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        try{
            initParams(req);
        }catch (Exception e){
            e.printStackTrace();
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
        MessageSystem.INSTANCE.sendMessageForService(new MessageToMoveWordsToLearned(null, AddressService.INSTANCE.getTrainingServiceAddress(),
                userId, trainingId, wordIds));
    }

    private void initParams(HttpServletRequest request) {
        wordIds = Arrays.stream(request.getParameter("wordId").split(","))
                .map(Integer::parseInt).collect(Collectors.toList());
        trainingId = Integer.parseInt(request.getParameter("trainingId"));
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
    }
}
