package controllers;

import java.io.IOException;
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
import dao.UserDAO;
import utils.ConnectionHandler;
import utils.PathUtils;
import utils.TemplateHandler;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private TemplateEngine templateEngine;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
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
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		if(username == null || password == null) {
			forwardToErrorPage(request, response, "Register module missing some data");
			return;
		}
		
		
		UserDAO userDAO = new UserDAO(connection);
		

		User user = null;
		
		try {
			user = userDAO.getAccountById(username);
		} catch (SQLException e) {
			forwardToErrorPage(request, response, e.getMessage());
			return;	
		}
		
		if(user != null) {
			request.setAttribute("warning", "Username already exists!");
			forward(request, response, PathUtils.pathToRegisterPage);
			return;
		}
		
		
		try {
			userDAO.register(username, password);
		} catch (SQLException e) {
			forwardToErrorPage(request, response, e.getMessage());
			return;	
		}
		
		
		try {
			user = userDAO.getAccountById(username);
		} catch (SQLException e) {
			forwardToErrorPage(request, response, e.getMessage());
			return;	
		}
	
		HttpSession session = request.getSession();
		session.setAttribute("currentUser", user);
		response.sendRedirect(getServletContext().getContextPath() + PathUtils.goToHomeServletPath);
		
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