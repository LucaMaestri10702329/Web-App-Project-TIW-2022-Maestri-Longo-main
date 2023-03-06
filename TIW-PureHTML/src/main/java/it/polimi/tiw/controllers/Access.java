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

import it.polimi.tiw.DAO.*;
import it.polimi.tiw.beans.*;
import it.polimi.tiw.utils.ConnectionHandler;
import it.polimi.tiw.utils.PathUtils;
import it.polimi.tiw.utils.TemplateHandler;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Access")
public class Access extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private TemplateEngine templateEngine;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Access() {
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
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String DocumentIds = request.getParameter("documentId");
		int documentId = Integer.parseInt(DocumentIds);
		
		DocumentDAO documentDAO = new DocumentDAO(connection);
		Document document = null;
		
		try {
			document = documentDAO.getDocumentById(documentId);			
		} catch (SQLException e) {
			forwardToErrorPage(request, response, e.getMessage());
			return;
		}
		
		String path = "/WEB-INF/document.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("currentDocument", document);
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
}
