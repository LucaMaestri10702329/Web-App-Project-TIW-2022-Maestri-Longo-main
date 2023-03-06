package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import it.polimi.tiw.DAO.FolderDAO;
import it.polimi.tiw.beans.Folder;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.utils.ConnectionHandler;
import it.polimi.tiw.utils.PathUtils;
import it.polimi.tiw.utils.TemplateHandler;

/**
 * Servlet implementation class ToRegisterPage
 */
@WebServlet("/GoToHome")
public class GoToHome extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToHome() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init() throws ServletException {
    	ServletContext servletContext = getServletContext();
		this.templateEngine = TemplateHandler.getEngine(servletContext, ".html");
		this.connection = ConnectionHandler.getConnection(servletContext);
    }
    
    @Override
    public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		
		HttpSession session = request.getSession(false);
		User currentUser = (User)session.getAttribute("currentUser");
		List<Folder> allFolders = null;
		List<Folder> topFolders = null;
		boolean seleziona = false;
		FolderDAO folderDAO = new FolderDAO(connection);
		try {
			allFolders = folderDAO.findAllFoldersById(currentUser.getId());
			topFolders = folderDAO.findTopFolderAndSubfoldersById(currentUser.getId());
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Error in retrieving products from the database");
			return;
		}
		// Redirect to the Home page and add folders to the parameters
		String path = "/WEB-INF/Home.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("allFolders", allFolders);
		ctx.setVariable("topFolders", topFolders);
		ctx.setVariable("seleziona", seleziona);
		templateEngine.process(path, ctx, response.getWriter());
	}
	
	private void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException{
		
		request.setAttribute("error", error);
		forward(request, response, PathUtils.pathToErrorPage);
		return;
	}
	
	private void forward(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException{
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		templateEngine.process(path, ctx, response.getWriter());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
