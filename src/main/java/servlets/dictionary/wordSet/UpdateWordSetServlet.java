package servlets.dictionary.wordSet;

import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MsgUpdateWordSet;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import servlets.NonAbonentServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервлет, обрабатывающий запрос на изменение набора
 */
public class UpdateWordSetServlet extends HttpServlet implements NonAbonentServlet {
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
    private HttpServletResponse response;
    private int wordSetId;
    private String name;
    private FileItem img;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        response = resp;
        String sessionId = req.getSession().getId();

        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        initParams(req);

        createMessage();
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.flushBuffer();
    }

    @Override
    public void createMessage() {
        MessageSystem.INSTANCE.sendMessageForService(new MsgUpdateWordSet(null, AddressService.INSTANCE.getDictionaryServiceAddress(),
                wordSetId, img, name));
    }

    public void handle(boolean status){
        try {
            response.setStatus(status ?
                    HttpServletResponse.SC_OK :
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.flushBuffer();
        } catch (IOException e) {
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "", e);
        }
    }

    private void initParams(HttpServletRequest request) {
        img = getImg(request);
        name = request.getParameter("name");
        wordSetId = Integer.parseInt(request.getHeader("wordSetId"));
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
}
