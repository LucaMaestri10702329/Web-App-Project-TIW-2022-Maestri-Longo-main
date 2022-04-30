package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import beans.Document;

public class DocumentDAO {
	private Connection connection;

	public DocumentDAO(Connection connection) {
		this.connection = connection;
	}
	
	public List<Document> getDocumentByIdFolder(int idFolder) throws SQLException{

		List<Document> documents = new ArrayList<>();
		String performedAction = " finding documents by account id";
		String query = "SELECT * FROM prject.document WHERE idfolder = ?";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, idFolder);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				Document document = new Document();
				document.setIdDocument(resultSet.getInt("idDocument"));
				document.setNome(resultSet.getString("nome"));
				document.setIdFolder(resultSet.getInt("idFolder"));
				
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
	
	public void createDocument(int idFolder, int idDocument, String nome) throws SQLException{

		String performedAction = " creating a new document";
		String UserInsert = "INSERT INTO project.document(idFolder, idDocument, String nome) VALUES(?,?,?)";
		
		PreparedStatement preparedStatementInsert = null;	
	
		try {
			connection.setAutoCommit(false);
			preparedStatementInsert = connection.prepareStatement(UserInsert);
		
			preparedStatementInsert.setInt(1, idFolder);
			preparedStatementInsert.setInt(2, idDocument);
			preparedStatementInsert.setString(3, nome);
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
	
	public void moveDocument(int idFolderD, int idDocument, String nome) throws SQLException
	{
		String performedAction = " moving the document";
		String moveDocument = "UPDATE project.document SET idFolder = ?  WHERE idDocument = ?";
		
		PreparedStatement preparedStatementMoveDocument= null;	

		try {
			connection.setAutoCommit(false);
			preparedStatementMoveDocument = connection.prepareStatement(moveDocument);
			
			
			preparedStatementMoveDocument.setInt(1, idFolderD);
			preparedStatementMoveDocument.setInt(2, idDocument);
			preparedStatementMoveDocument.executeUpdate();
			
			
			connection.commit();
			
		}catch(SQLException e) {
			connection.rollback();
			
			throw new SQLException("Error accessing the DB when" + performedAction);
		}finally {
			connection.setAutoCommit(true);
			try {
				preparedStatementMoveDocument.close();
			
			}catch (Exception e) {
				throw new SQLException("Error closing the statement when" + performedAction);
			}
		}
	}
}
