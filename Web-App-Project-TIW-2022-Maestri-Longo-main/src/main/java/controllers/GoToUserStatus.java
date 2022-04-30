package controllers;

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

import beans.User;
import beans.Document;
import dao.UserDAO;
import dao.DocumentDAO;
import dao.FolderDAO;
import beans.Folder;
import utils.ConnectionHandler;
import utils.PathUtils;
import utils.TemplateHandler;

/**
 * Servlet implementation class ToRegisterPage
 */
@WebServlet("/GoToAccountStatus")
public class GoToUserStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToUserStatus() {
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
		
		
		String accountIdString = request.getParameter("accountId");
		
		if(accountIdString == null) {
			forwardToErrorPage(request, response, "Null account id, when accessing account details");
		}
		
		
		
		HttpSession session = request.getSession(false);
		User currentUser = (User)session.getAttribute("currentUser");
		
		UserDAO userDAO = new UserDAO(connection);
		User user;
		try {
			user = userDAO.getAccountById(accountIdString);
		} catch (SQLException e) {
			forwardToErrorPage(request, response, e.getMessage());
			return;		
		}
		
		if(user == null || user.getUsername() != currentUser.getUsername()) {
			forwardToErrorPage(request, response, "Account not existing or not yours");
			return;
		}
		
		List<Folder> folders;
		FolderDAO folderDAO = new FolderDAO(connection);
		try {
			folders = folderDAO.getFolderByUsername(accountIdString);
		}catch (SQLException e) {
			forwardToErrorPage(request, response, e.getMessage());
			return;	
		}
		
		
		request.setAttribute("user", user);
		request.setAttribute("folders", folders);
		forward(request, response, PathUtils.pathToAccount);
		
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