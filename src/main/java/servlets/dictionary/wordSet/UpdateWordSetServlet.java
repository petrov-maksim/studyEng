package servlets.dictionary.wordSet;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MessageUpdateWordSet;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import servlets.NonAbonentServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class UpdateWordSetServlet extends HttpServlet implements NonAbonentServlet {
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

    private static final Address address = new Address();
    private HttpServletResponse response;
    private String sessionId;

    private int wordSetId;
    private String name;
    private FileItem img;



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        response = resp;
        sessionId = req.getSession().getId();

        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        try {
            initParams(req);
        } catch (Exception e) {
            System.out.println("Wrong parameters in UpdateWordSetServlet");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.flushBuffer();
            return;
        }

        createMessage();
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.flushBuffer();
    }

    @Override
    public void createMessage() {
        MessageSystem.INSTANCE.sendMessageForService(new MessageUpdateWordSet(null, AddressService.INSTANCE.getDictionaryServiceAddress(),
                wordSetId, img, name));
    }

    public void handle(boolean status){
        int sc = status ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        try {
            response.setStatus(sc);
            response.getWriter().write("Handled anyway");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initParams(HttpServletRequest request) throws Exception {
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
