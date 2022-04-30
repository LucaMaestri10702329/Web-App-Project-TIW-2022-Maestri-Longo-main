package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Folder;

public class FolderDAO {

		private Connection connection;

		public FolderDAO(Connection connection) {
			this.connection = connection;
		}
		
		public List<Folder> getFolderByUsername(String username) throws SQLException{

			List<Folder> folders = new ArrayList<>();
			String performedAction = " finding folder by username";
			String query = "SELECT * FROM project.folder WHERE idUser = ? ";
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			
			try {
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, username);
				
				resultSet = preparedStatement.executeQuery();
				
				while(resultSet.next()) {
					Folder folder = new Folder();
					folder.setIdFolder(resultSet.getInt("idFolder"));
					folder.setIdFolderMother(resultSet.getString("idFolderMother"));
					folder.setName(resultSet.getString("name"));
					folder.setIdUsername(resultSet.getString("idUser"));
					
					folders.add(folder);
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
			return folders;
		}
		
		public void createFolder(int idFolder, int idFolderMother, String nome, String username) throws SQLException{

			String performedAction = " creating a new folder";
			String FolderInsert = "INSERT INTO project.folder (idFolder, idFolderMother, name, idUser) VALUES(?,?,?,?)";
			
			PreparedStatement preparedStatementInsert = null;	
		
			
			try {
				connection.setAutoCommit(false);
				preparedStatementInsert = connection.prepareStatement(FolderInsert);
			
				
				preparedStatementInsert.setInt(1, idFolder);
				preparedStatementInsert.setInt(2, idFolderMother);
				preparedStatementInsert.setString(3, nome);
				preparedStatementInsert.setString(4, username);
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
		
		public List<Folder> getFolderByFolder(int idFolder) throws SQLException{

			List<Folder> folders = new ArrayList<>();
			String performedAction = " finding folder by username";
			String query = "SELECT * FROM project.folder WHERE idFolderMother = ? ";
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			
			try {
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setInt(1, idFolder);
				
				resultSet = preparedStatement.executeQuery();
				
				while(resultSet.next()) {
					Folder folder = new Folder();
					folder.setIdFolder(resultSet.getInt("idFolder"));
					folder.setIdFolderMother(resultSet.getString("idFolderMother"));
					folder.setName(resultSet.getString("name"));
					folder.setIdUsername(resultSet.getString("idUser"));
					
					folders.add(folder);
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
			return folders;
		}
}
