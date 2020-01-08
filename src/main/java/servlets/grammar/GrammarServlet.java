package servlets.grammar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class GrammarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path [];
        if (request.getPathInfo() == null)
            handleGrammarRequest(request, response);
        else if ((path = request.getPathInfo().substring(1).split("/")).length == 1)
            handleBlockRequest(request, response, path);
        else  if (path.length == 2)
            handleLessonRequest(request, response, path);
    }

    private void handleGrammarRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().write("root");
    }

    private void handleBlockRequest(HttpServletRequest request, HttpServletResponse response, String path[]) throws IOException {
        response.getWriter().write(Arrays.toString(path));
    }

    private void handleLessonRequest(HttpServletRequest request, HttpServletResponse response, String path[]) throws IOException {
        response.getWriter().write(Arrays.toString(path));
    }
}
