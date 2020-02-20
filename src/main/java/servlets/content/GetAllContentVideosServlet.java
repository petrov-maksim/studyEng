package servlets.content;

import entities.content.Text;
import entities.content.Video;
import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.content.toService.MessageGetAllContentVideos;
import servlets.BaseServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GetAllContentVideosServlet extends HttpServlet implements BaseServlet {
    private static final Address address = new Address();
    private HttpServletResponse response;
    private String sessionId;
    private int userId;
    private int index;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        response = resp;
        sessionId = req.getSession().getId();
        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        //First request
        if (req.getHeader("handling") == null) {
            try{
                initParams(req);
            }catch (Exception e){
                System.out.println("Wrong parameters in GetAllContentVideosServlet");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.flushBuffer();
                return;
            }
            createMessage();

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.flushBuffer();
        }
        //Not the first request
        else
            checkServiceResult();
    }

    public void handle(List<Video> videos) {
        response.setHeader("ready", "true");
        System.out.println(response);
        try {
            response.getWriter().write("Hello");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createMessage() {
        MessageSystem.INSTANCE.sendMessageForService(new MessageGetAllContentVideos(getAdr(), AddressService.INSTANCE.getContentServiceAddress(),
                sessionId, userId, index));
    }

    private void initParams(HttpServletRequest request){
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
        index = Integer.parseInt(request.getParameter("index"));
    }

    @Override
    public void checkServiceResult() {
        MessageSystem.INSTANCE.execForServlet(this);
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public Address getAddress() {
        return getAdr();
    }

    public static Address getAdr(){
        return address;
    }
}
