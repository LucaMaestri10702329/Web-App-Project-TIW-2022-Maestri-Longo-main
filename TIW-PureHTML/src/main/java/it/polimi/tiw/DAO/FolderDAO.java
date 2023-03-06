package it.polimi.tiw.DAO;

import java.util.List;

import it.polimi.tiw.beans.Folder;
import it.polimi.tiw.beans.User;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FolderDAO {
	private Connection connection;

	public FolderDAO() {
		// TODO Auto-generated constructor stub
	}

	public FolderDAO(Connection con) {
		this.connection = con;
	}

	public List<Folder> findAllFolders() throws SQLException {
		List<Folder> folders = new ArrayList<Folder>();
		try (PreparedStatement pstatement = connection.prepareStatement("SELECT * FROM drive.folder");) {
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Folder folder = new Folder();
					folder.setId(result.getInt("id"));
					folder.setUserId(result.getInt("userId"));
					folder.setName(result.getString("name"));
					folder.setDate(result.getDate("date"));
					folders.add(folder);
				}
			}
		}
		return folders;
	}
	
	public List<Folder> findAllFoldersById(int id) throws SQLException {
		List<Folder> folders = new ArrayList<Folder>();
		
		String query = "SELECT * FROM drive.folder where userId = ?";
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			result = preparedStatement.executeQuery();
			
			while (result.next()) {
				Folder folder = new Folder();
				folder.setId(result.getInt("id"));
				folder.setUserId(result.getInt("userId"));
				folder.setName(result.getString("name"));
				folder.setDate(result.getDate("date"));
				folders.add(folder);
			}
			
		}catch(SQLException e) {
			throw new SQLException("Error accessing the DB when");
		}finally {
			try {
				result.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the result set when");
			}
			try {
				preparedStatement.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the statement when");
			}
		}
		return folders;
	}

	
	public Folder findFolderById(int id) throws SQLException {
		Folder folder = new Folder();
		
		String query = "SELECT * FROM drive.folder where id = ?";
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			result = preparedStatement.executeQuery();
			
			while (result.next()) {
				folder.setId(result.getInt("id"));
				folder.setUserId(result.getInt("userId"));
				folder.setName(result.getString("name"));
				folder.setDate(result.getDate("date"));
			}
			
		}catch(SQLException e) {
			throw new SQLException("Error accessing the DB when");
		}finally {
			try {
				result.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the result set when");
			}
			try {
				preparedStatement.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the statement when");
			}
		}
		return folder;
	}
	
	public List<Folder> findTopFolderAndSubfoldersById(int id) throws SQLException {

		
		List<Folder> folders = new ArrayList<Folder>();
		
		String query = "SELECT * FROM drive.folder WHERE id NOT IN (SELECT idsubfolder FROM drive.subfolder ) and userId = ?";
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			result = preparedStatement.executeQuery();
			
			while (result.next()) {
				Folder folder = new Folder();
				folder.setId(result.getInt("id"));
				folder.setUserId(result.getInt("userId"));
				folder.setName(result.getString("name"));
				folder.setDate(result.getDate("date"));
				folder.setIsTop(true);
				folders.add(folder);
			}
			
			for (Folder p : folders) {
				findSubparts(p);
			}
			
		}catch(SQLException e) {
			throw new SQLException("Error accessing the DB when");
		}finally {
			try {
				result.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the result set when");
			}
			try {
				preparedStatement.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the statement when");
			}
		}
		return folders;
	}
	
	public List<Folder> findTopFolderAndSubfolders() throws SQLException {
		List<Folder> folders = new ArrayList<Folder>();
		try (PreparedStatement pstatement = connection
				.prepareStatement("SELECT * FROM drive.folder WHERE id NOT IN (SELECT idsubfolder FROM drive.subfolder )");) {
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					Folder folder = new Folder();
					folder.setId(result.getInt("id"));
					folder.setUserId(result.getInt("userId"));
					folder.setName(result.getString("name"));
					folder.setDate(result.getDate("date"));
					folder.setIsTop(true);
					folders.add(folder);
				}
				for (Folder p : folders) {
					findSubparts(p);
				}
			}
		}
		return folders;
	}

	public void findSubparts(Folder p) throws SQLException {
		Folder folder = null;
		try (PreparedStatement pstatement = connection.prepareStatement(
				"SELECT P.id, P.userId, P.name, P.date FROM drive.subfolder S JOIN drive.folder P on P.id = S.idsubfolder WHERE S.idFather = ?");) {
			pstatement.setInt(1, p.getId());
			try (ResultSet result = pstatement.executeQuery();) {
				while (result.next()) {
					folder = new Folder();
					folder.setId(result.getInt("id"));
					folder.setUserId(result.getInt("userId"));
					folder.setName(result.getString("name"));
					folder.setDate(result.getDate("date"));
					p.addSubfolder(folder);
				}
			}
		}

	}

	public void createFolder(int userId, String name, Date date) throws SQLException {
		String query = "insert into drive.folder (userId,name,date) values (?,?,?)";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, userId);
			pstatement.setString(2, name);
			pstatement.setDate(3, date);
			pstatement.executeUpdate();
		}
	}
	
	public void createSubFolder(int userId, String name, Date date,int idFather) throws SQLException {
		String query = "insert into drive.folder (userId,name,date) values (?,?,?)";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, userId);
			pstatement.setString(2, name);
			pstatement.setDate(3, date);
			pstatement.executeUpdate();
		}
		int idSubFolder = findAllFoldersById(userId).get(findAllFoldersById(userId).size()-1).getId();
		query = "insert into drive.subfolder (idsubfolder,idfather) values (?,?)";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, idSubFolder);
			pstatement.setInt(2, idFather);
			pstatement.executeUpdate();
		}
	}
	
	public List<Folder> findSubfoldersById(int id) throws SQLException {

		
		List<Folder> folders = new ArrayList<Folder>();
		
		String query = "SELECT * FROM drive.folder WHERE id  IN (select folder.id from drive.folder inner  join drive.subfolder on subfolder.idsubfolder = folder.id) and userId = ?";
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			result = preparedStatement.executeQuery();
				while (result.next()) {
					Folder folder = new Folder();
					folder.setId(result.getInt("id"));
					folder.setUserId(result.getInt("userId"));
					folder.setName(result.getString("name"));
					folder.setDate(result.getDate("date"));
					folder.setIsTop(true);
					folders.add(folder);
				}
				for (Folder p : folders) {
					findSubparts(p);
				}
			}catch(SQLException e) {
				throw new SQLException("Error accessing the DB when");
			}finally {
				try {
					result.close();
				}catch (Exception e) {
					throw new SQLException("Error closing the result set when");
				}
				try {
					preparedStatement.close();
				}catch (Exception e) {
					throw new SQLException("Error closing the statement when");
				}
			}
		return folders;
	}
	
}
