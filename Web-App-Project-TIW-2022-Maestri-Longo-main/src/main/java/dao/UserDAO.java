package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.User;

public class UserDAO {
	private Connection connection;

	public UserDAO(Connection connection) {
		this.connection = connection;
	}
	
	public User getAccountById(String username) throws SQLException{

		User user = null;
		String performedAction = " finding an user by username";
		String query = "SELECT * FROM project.user WHERE username = ?";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				user = new User();
				user.setUsername(resultSet.getString("username"));
				user.setPwd(resultSet.getString("pwd"));
			}
			
		}catch(SQLException e) {
			throw new SQLException("Error accessing the DB when" + performedAction);
		}finally {
			try {
				resultSet.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the result set when" + performedAction);
			}
			try {
				preparedStatement.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the statement when" + performedAction);
			}
		}
		return user;
	}
	
	public void register(String username, String pwd) throws SQLException{

		String performedAction = " creating a new document";
		String UserInsert = "INSERT INTO project.user(username, pwd) VALUES(?,?)";
		
		PreparedStatement preparedStatementInsert = null;	
	
		try {
			connection.setAutoCommit(false);
			preparedStatementInsert = connection.prepareStatement(UserInsert);
		
			preparedStatementInsert.setString(1, username);
			preparedStatementInsert.setString(2, pwd);
			preparedStatementInsert.executeUpdate();
			
			connection.commit();
			
		}catch(SQLException e) {
			connection.rollback();
			
			throw new SQLException("Error accessing the DB when" + performedAction);
		}finally {
			connection.setAutoCommit(true);
			try {
				preparedStatementInsert.close();
				
			}catch (Exception e) {
				throw new SQLException("Error closing the statement when" + performedAction);
			}
		}
	}
	public User getAccountByIdAndPwd(String username,String pwd) throws SQLException{

		User user = null;
		String performedAction = " finding an user by login";
		String query = "SELECT * FROM project.user WHERE username = ? & WHERE pwd = ?";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, pwd);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				user = new User();
				user.setUsername(resultSet.getString("username"));
				user.setPwd(resultSet.getString("pwd"));
			}
			
		}catch(SQLException e) {
			throw new SQLException("Error accessing the DB when" + performedAction);
		}finally {
			try {
				resultSet.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the result set when" + performedAction);
			}
			try {
				preparedStatement.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the statement when" + performedAction);
			}
		}
		return user;
	}
}
