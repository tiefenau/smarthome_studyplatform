package de.pfiva.data.ingestion.model;

public class InputFile {

	private String filename;
	private String filePath;
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	@Override
	public String toString() {
		return "InputFile [filename=" + filename + ", filePath=" + filePath + "]";
	}
}
