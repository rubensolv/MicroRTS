package ai.synthesis.twophasessa.scriptInterface.gui;

public class MapPath {
	
	
	private String name;
	private String path;
	
	
	public MapPath(String name, String path) {
		this.name = name;
		this.path = path;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return getName();
	}
	
	
}
