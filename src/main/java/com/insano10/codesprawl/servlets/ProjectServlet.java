package com.insano10.codesprawl.servlets;

import com.google.gson.Gson;
import com.insano10.codesprawl.source.FileInspector;
import com.insano10.codesprawl.source.ProjectFile;
import com.insano10.codesprawl.source.VcsInspector;
import com.insano10.codesprawl.source.VcsSystem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

public class ProjectServlet extends HttpServlet
{
    private static final Gson GSON = new Gson();
    private static final FileInspector FILE_INSPECTOR = new FileInspector();
    private static final VcsInspector VCS_INSPECTOR = new VcsInspector();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if(request.getRequestURI().endsWith("/definition"))
        {
            Collection<ProjectFile> files = FILE_INSPECTOR.getFiles();
            VCS_INSPECTOR.inspectVcs();

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
            ProjectDefinitionResponse projectResponse = GSON.fromJson(request.getParameter("configuration"), ProjectDefinitionResponse.class);

            FILE_INSPECTOR.setSourcePath(projectResponse.getSourceDirectoryPath());
            FILE_INSPECTOR.setFileExtensions(projectResponse.getFileExtensions());

            VCS_INSPECTOR.setVcsRoot(projectResponse.getVcsRootPath());
            VCS_INSPECTOR.setSystem(projectResponse.getVcsOption());

            response.getWriter().print(GSON.toJson(""));
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else
        {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
