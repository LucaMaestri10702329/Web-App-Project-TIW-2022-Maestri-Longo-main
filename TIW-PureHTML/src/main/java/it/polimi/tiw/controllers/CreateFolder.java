package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringEscapeUtils;

import it.polimi.tiw.DAO.*;
import it.polimi.tiw.beans.User;

@WebServlet("/CreateFolder")
public class CreateFolder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;

	public CreateFolder() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() throws ServletException {
		try {
			ServletContext context = getServletContext();
			String driver = context.getInitParameter("dbDriver");
			String url = context.getInitParameter("dbUrl");
			String user = context.getInitParameter("dbUser");
			String password = context.getInitParameter("dbPassword");
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new UnavailableException("Can't load database driver");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UnavailableException("Couldn't get db connection");
		}

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String name = null;
		Date date = null;
		int userId = 0;
		boolean badRequest = false;
		try {
			name = request.getParameter("name");
			long miliseconds = System.currentTimeMillis();
	        date = new Date(miliseconds);
	        System.out.println(date);
			userId = Integer.parseInt(request.getParameter("userId"));
			
			if (name.isEmpty()) {
				badRequest = true;
			}
			
		} catch (NullPointerException | NumberFormatException e) {
			badRequest = true;
		}
		
		if (badRequest) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or incorrect parameters");
			return;
		}
		
		FolderDAO folderDAO = new FolderDAO(connection);
		try {
			folderDAO.createFolder(userId,name,date);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Error in creating the product in the database");
			return;
		}
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToHome";
		response.sendRedirect(path);
	}
	
	@Override
	public void destroy() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e){
				
			}
		}
	}

}
