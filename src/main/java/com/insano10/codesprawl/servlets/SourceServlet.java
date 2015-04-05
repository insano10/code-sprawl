package com.insano10.codesprawl.servlets;

import com.google.gson.Gson;
import com.insano10.codespawl.source.SourceInspector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SourceServlet extends HttpServlet
{
    private static final Gson GSON = new Gson();
    private static final SourceInspector SOURCE_INSPECTOR = new SourceInspector();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if(request.getRequestURI().endsWith("/source"))
        {
            String inspectionResponse = SOURCE_INSPECTOR.inspect();
            response.getWriter().print(GSON.toJson(inspectionResponse));
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else
        {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if(request.getRequestURI().endsWith("/source"))
        {
            final String sourceLocation = request.getParameter("location");
            final Path sourcePath = Paths.get(sourceLocation);
            SOURCE_INSPECTOR.setPath(sourcePath);

            response.getWriter().print(GSON.toJson(""));
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else
        {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
