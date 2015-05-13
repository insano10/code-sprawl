package com.insano10.codesprawl.servlets;

import com.google.gson.Gson;
import com.insano10.codesprawl.source.ProjectFile;
import com.insano10.codesprawl.source.FileInspector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

public class ProjectServlet extends HttpServlet
{
    private static final Gson GSON = new Gson();
    private static final FileInspector FILE_INSPECTOR = new FileInspector();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if(request.getRequestURI().endsWith("/definition"))
        {
            Collection<ProjectFile> files = FILE_INSPECTOR.getFiles();
            response.getWriter().print(GSON.toJson(files));
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
        if(request.getRequestURI().endsWith("/definition/configuration"))
        {
            final String sourceLocation = request.getParameter("sourceLocation");
            final String[] fileExtensions = request.getParameterValues("fileExtensions[]");
            final Path sourcePath = Paths.get(sourceLocation);

            System.out.println(Arrays.toString(fileExtensions));

            FILE_INSPECTOR.setSourcePath(sourcePath);

            response.getWriter().print(GSON.toJson(""));
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else
        {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
