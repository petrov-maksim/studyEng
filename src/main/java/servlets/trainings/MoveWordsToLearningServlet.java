package servlets.trainings;

import messageSystem.MessageSystem;
import messageSystem.messages.trainings.toService.MsgToMoveWordsToLearning;
import servlets.NonAbonentServlet;
import util.AddressService;
import util.SessionCache;
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
 * Сервлет, обрабатывающий запрос на перевод слов в состояние "на изучении"
 */
public class MoveWordsToLearningServlet extends HttpServlet implements NonAbonentServlet {
    private String sessionId;
    private int userId;
    private int trainingId;
    private List<Integer> wordIds;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        sessionId = req.getSession().getId();

        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        try{
            initParams(req);
        }catch (Exception e){
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE,
                    "Wrong parameters:\n" + "wordIds: " + wordIds +
                            "trainingId: " + trainingId, e);
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
        MessageSystem.INSTANCE.sendMessageForService(new MsgToMoveWordsToLearning(null, AddressService.INSTANCE.getTrainingServiceAddress(),
                userId, trainingId, wordIds));
    }

    private void initParams(HttpServletRequest request) {
        wordIds = Arrays.stream(request.getParameter("wordId").split(","))
                .map(Integer::parseInt).collect(Collectors.toList());
        trainingId = Integer.parseInt(request.getParameter("trainingId"));
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
    }
}