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
import dao.DocumentDAO;
import utils.ConnectionHandler;
import utils.PathUtils;
import utils.TemplateHandler;

/**
 * Servlet implementation class ToRegisterPage
 */
@WebServlet("/CreateDocument")
public class CreateFolder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;
	private String accountUsername;
	
	 public CreateFolder() {
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
			request.setAttribute("accountId", accountUsername);
			forward(request, response, PathUtils.pathToCreateFolderFailed);
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
		
		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			String sourceUsername = request.getParameter("sourceUsername");
			String name = request.getParameter("name");
			String idFolderString = request.getParameter("destFolder");
			String idFolderMotherString = request.getParameter("ifFolderMother");
			
			if(sourceUsername == null || name == null || idFolderString == null) {
				forwardToErrorPage(request, response, "Some data requested is null, when making a transfer");
				return;
			}
			
			int idFolder;
			int idFolderMother;
			try {
				idFolder = Integer.parseInt(idFolderString);
				idFolderMother = Integer.parseInt(idFolderMotherString);
				
			}catch (NumberFormatException e) {
				forwardToErrorPage(request, response, "Some values requested are not numbers, when making a transfer");
				return;
			}
			
			HttpSession session = request.getSession(false);
			User currentUser = (User)session.getAttribute("currentUser");
				
			UserDAO userDAO = new UserDAO(connection);
			User sourceAccount;
			
			try {
				sourceAccount = userDAO.getAccountById(sourceUsername);
			} catch (SQLException e) {
				forwardToErrorPage(request, response, e.getMessage());
				return;
			}
			
			if(sourceAccount == null) {
				forwardToTransferFailedPage(request, response, "User not found");
				return;
			}
			if(sourceAccount.getUsername() != currentUser.getUsername()) {
				forwardToTransferFailedPage(request, response, "The destination owner is not the current user");
				return;
			}
			FolderDAO folderDAO = new FolderDAO(connection);
			try {
				folderDAO.createFolder(idFolder,idFolderMother,name,sourceUsername);
			} catch (SQLException e) {
				forwardToErrorPage(request, response, e.getMessage());
				return;		
			}
			
			Folder folder = new Folder();
			folder.setIdFolder(idFolder);
			folder.setIdFolderMother(idFolderMother);
			folder.setName(name);
			
			session.setAttribute("idFolder", idFolder);
			session.setAttribute("idFolderMother", idFolderMother);
			
			session.setAttribute("folderInCreation", false);
			
			response.sendRedirect(getServletContext().getContextPath() + PathUtils.pathToMoveDocumentConfirmedPage);
			}
}