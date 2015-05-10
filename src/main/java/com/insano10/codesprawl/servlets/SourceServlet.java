package com.insano10.codesprawl.servlets;

import com.google.gson.Gson;
import com.insano10.codesprawl.source.CodeUnit;
import com.insano10.codesprawl.source.SourceInspector;
import com.insano10.codesprawl.source.language.JavaFileInspector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

public class SourceServlet extends HttpServlet
{
    private static final Gson GSON = new Gson();
    private static final SourceInspector SOURCE_INSPECTOR = new SourceInspector();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if(request.getRequestURI().endsWith("/definition"))
        {
            Collection<CodeUnit> inspectionResponse = SOURCE_INSPECTOR.inspect();
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
        if(request.getRequestURI().endsWith("/definition/location"))
        {
            final String sourceLocation = request.getParameter("sourceLocation");
            final Path sourcePath = Paths.get(sourceLocation);

            SOURCE_INSPECTOR.reset();
            SOURCE_INSPECTOR.addFileInspector(new JavaFileInspector(sourcePath));

            response.getWriter().print(GSON.toJson(""));
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else
        {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
