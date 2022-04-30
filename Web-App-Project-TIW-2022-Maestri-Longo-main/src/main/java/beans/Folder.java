package beans;

public class Folder {
	
	private int IdFolder;
	private String IdUsername;
	private String IdFolderMother;
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
	public String getIdFolderMother() {
		return IdFolderMother;
	}
	public void setIdFolderMother(String idFolderMother) {
		IdFolderMother = idFolderMother;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
