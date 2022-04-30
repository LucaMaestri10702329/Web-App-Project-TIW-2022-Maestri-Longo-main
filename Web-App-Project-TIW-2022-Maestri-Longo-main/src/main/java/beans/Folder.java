package beans;

public class Folder {
	
	private int IdFolder;
	private String IdUsername;
	private int IdFolderMother;
	private String name;
	
	public int getIdFolder() {
		return IdFolder;
	}
	public void setIdFolder(int idFolder) {
		IdFolder = idFolder;
	}
	public String getIdUsername() {
		return IdUsername;
	}
	public void setIdUsername(String idUsername) {
		IdUsername = idUsername;
	}
	public int getIdFolderMother() {
		return IdFolderMother;
	}
	public void setIdFolderMother(int idFolderMother) {
		IdFolderMother = idFolderMother;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
