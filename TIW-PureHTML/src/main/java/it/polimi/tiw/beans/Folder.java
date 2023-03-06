package it.polimi.tiw.beans;

import java.util.ArrayList;
import java.io.Serializable;
import java.sql.Date;

public class Folder implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private int userId;
	private Boolean isTop = false;
	private String name;
	private Date date;
	private ArrayList<Folder> subfolders = new ArrayList<Folder>();

	public Folder() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int idproduct) {
		this.id = idproduct;
	}


	public Boolean getIsTop() {
		return this.isTop;
	}

	public void setIsTop(Boolean ist) {
		this.isTop = ist;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String toString() {
		StringBuffer aBuffer = new StringBuffer("BomProduct");
		aBuffer.append(" id: ");
		aBuffer.append(id);
		aBuffer.append(" name: ");
		aBuffer.append(name);
		aBuffer.append(" subparts: ");
		return aBuffer.toString();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public ArrayList<Folder> getSubfolders() {
		return subfolders;
	}

	public void setSubfolders(ArrayList<Folder> subfolders) {
		this.subfolders = subfolders;
	}
	
	public void addSubfolder(Folder part) {
		subfolders.add(part);
	}

}