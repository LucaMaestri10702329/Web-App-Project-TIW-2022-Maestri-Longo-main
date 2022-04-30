package controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import beans.User;
import beans.Document;
import beans.User;
import beans.Folder;
import dao.FolderDAO;
import dao.UserDAO;
import dao.FolderDAO;
import utils.ConnectionHandler;
import utils.PathUtils;
import utils.TemplateHandler;

/**
 * Servlet implementation class ToRegisterPage
 */
@WebServlet("/CreateDocument")
public class CreateDocument extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;
	
	 public CreateDocument() {
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
	    
	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doPost(request, response);
		}
		
		private void forwardToTransferFailedPage(HttpServletRequest request, HttpServletResponse response, String reasonOfFailure) throws ServletException, IOException{
			
			request.setAttribute("reason", reasonOfFailure);
			request.setAttribute("accountId", sourceAccountId);
			forward(request, response, PathUtils.pathToTransferFailedPage);
			return;
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