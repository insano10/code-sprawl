package com.insano10.codesprawl.servlets;

import com.google.gson.Gson;
import com.insano10.codesprawl.configuration.ConfigurationManager;
import com.insano10.codesprawl.source.FileInspector;
import com.insano10.codesprawl.source.ProjectFile;
import com.insano10.codesprawl.source.VcsInspector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class ProjectServlet extends HttpServlet
{
    private static final Gson GSON = new Gson();
    private static final FileInspector FILE_INSPECTOR = new FileInspector();
    private static final VcsInspector VCS_INSPECTOR = new VcsInspector();
    private static final ConfigurationManager CONFIG_MANAGER = new ConfigurationManager();

    @Override
    public void init() throws ServletException
    {
        super.init();

        CONFIG_MANAGER.loadConfiguration();
    }

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
        else if(request.getRequestURI().endsWith("/definition/configuration"))
        {
            final ProjectConfiguration configuration = CONFIG_MANAGER.getConfiguration();

            response.getWriter().print(GSON.toJson(configuration));
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
            ProjectConfiguration configuration = GSON.fromJson(request.getParameter("configuration"), ProjectConfiguration.class);

            CONFIG_MANAGER.saveConfiguration(configuration);

            FILE_INSPECTOR.setSourcePath(configuration.getSourceDirectoryPath());
            FILE_INSPECTOR.setFileExtensions(configuration.getFileExtensions());

            VCS_INSPECTOR.setVcsRoot(configuration.getVcsRootPath());
            VCS_INSPECTOR.setSystem(configuration.getVcsOption());

            response.getWriter().print(GSON.toJson(""));
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else
        {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
