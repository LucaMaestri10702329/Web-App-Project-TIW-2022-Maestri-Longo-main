package it.polimi.tiw.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import it.polimi.tiw.beans.Document;

import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DocumentDAO {
	private Connection connection;

	public DocumentDAO() {
		// TODO Auto-generated constructor stub
	}

	public DocumentDAO(Connection con) {
		this.connection = con;
	}
	public List<Document> getDocumentsByFolderId(int folderId) throws SQLException{

		List<Document> documents = new ArrayList<>();
		String performedAction = " finding a document by folderId";
		String query = "SELECT * FROM drive.document WHERE folderId = ?";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, folderId);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				Document document = new Document();
				document.setId(resultSet.getInt("id"));
				document.setName(resultSet.getString("name"));
				document.setFolderId(resultSet.getInt("folderId"));
				document.setSummary(resultSet.getString("summary"));
				document.setType(resultSet.getString("type"));
				document.setDate(resultSet.getDate("date"));
				documents.add(document);
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
		return documents;
	}
	
	public Document getDocumentById(int id) throws SQLException{

		Document document = null;
		String performedAction = " finding a document by id";
		String query = "SELECT * FROM drive.document WHERE id = ?";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				document = new Document();
				document.setId(resultSet.getInt("id"));
				document.setName(resultSet.getString("name"));
				document.setFolderId(resultSet.getInt("folderId"));
				document.setSummary(resultSet.getString("summary"));
				document.setType(resultSet.getString("type"));
				document.setDate(resultSet.getDate("date"));
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
		return document;
	}
	
	public void createDocument(String name, Date date, String summary, String type, int folderId) throws SQLException {
		String query = "insert into drive.document (name,date,summary,type,folderId) values (?,?,?,?,?)";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setString(1, name);
			pstatement.setDate(2, date);
			pstatement.setString(3, summary);
			pstatement.setString(4, type);
			pstatement.setInt(5, folderId);
			pstatement.executeUpdate();
		}
	}
	
	public void updateDocument(int id, int folderId) throws SQLException {
		String query = "UPDATE drive.document SET folderId = ? WHERE id = ? ";
		try (PreparedStatement pstatement = connection.prepareStatement(query);) {
			pstatement.setInt(1, folderId);
			pstatement.setInt(2, id);
			pstatement.executeUpdate();
		}
	}
}
