package servlets.dictionary.wordSet;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MsgAddWordSet;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import servlets.ServletAbonent;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервлет обрабатывающий запрос на добавление набора слов пользователю
 */
public class AddWordSetServlet extends ServletAbonent {
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

    private static final Address address = new Address();
    private HttpServletResponse response;
    private String sessionId;
    private String name;
    private int userId;
    private FileItem img;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        response = resp;
        sessionId = req.getSession().getId();
        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        //First request
        if (req.getHeader("handling") == null) {
            initParams(req);
            createMessage();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.flushBuffer();
        }
        //Not the first request
        else
            checkServiceResult();
    }

    @Override
    public void createMessage() {
        MessageSystem.INSTANCE.sendMessageForService(new MsgAddWordSet(getAdr(), AddressService.INSTANCE.getDictionaryServiceAddress(),
                img, name, sessionId, userId));
    }

    public void handle(int wordSetId){
        response.setStatus(wordSetId == -1 ?
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR :
                HttpServletResponse.SC_OK);
        response.setHeader("wordSetId", String.valueOf(wordSetId));
    }

    @Override
    public void notReady() {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader(READY, "false");
            response.flushBuffer();
        } catch (IOException e) {
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
        }
    }

    private void initParams(HttpServletRequest request) {
        img = getImg(request);
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
        name = request.getParameter("name");
    }

    private FileItem getImg(HttpServletRequest request){
        if(request.getHeader("img").equalsIgnoreCase("false"))
            return null;
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        factory.setRepository(new File(System.getProperty("catalina.base"), "temp"));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);

        try {
            return upload.parseRequest(request).get(0);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
        }
        return null;
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public Address getAddress() {
        return getAdr();
    }

    public static Address getAdr(){return address;}
}
