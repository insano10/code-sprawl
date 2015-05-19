package com.insano10.codesprawl.servlets;

import com.google.gson.Gson;
import com.insano10.codesprawl.configuration.ConfigurationManager;
import com.insano10.codesprawl.source.FileInspector;
import com.insano10.codesprawl.source.ProjectFile;
import com.insano10.codesprawl.vcs.VcsInspector;
import com.insano10.codesprawl.vcs.history.VcsTimeLine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProjectServlet extends HttpServlet
{
    private static final Gson GSON = new Gson();
    private static final Path DATA_DIR = Paths.get(System.getProperty("user.home"), ".code-sprawl");
    private static final FileInspector FILE_INSPECTOR = new FileInspector();
    private static final VcsInspector VCS_INSPECTOR = new VcsInspector(DATA_DIR);
    private static final ConfigurationManager CONFIG_MANAGER = new ConfigurationManager();

    @Override
    public void init() throws ServletException
    {
        super.init();

        CONFIG_MANAGER.addChangeListener(FILE_INSPECTOR);
        CONFIG_MANAGER.addChangeListener(VCS_INSPECTOR);
        CONFIG_MANAGER.loadConfiguration();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if(request.getRequestURI().endsWith("/definition"))
        {
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("files", FILE_INSPECTOR.getFiles());
            responseData.put("vcsTimeLine", VCS_INSPECTOR.getVcsTimeLine());

            response.getWriter().print(GSON.toJson(responseData));
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

            response.getWriter().print(GSON.toJson(""));
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else
        {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
