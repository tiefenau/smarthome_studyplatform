package de.pfiva.data.ingestion.model.snips;

import java.time.LocalDateTime;

public class InboundQueryData {

	private String filename;
	private String filePath;
	private String hotword;
	private LocalDateTime timestamp;
	
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
	public String getHotword() {
		return hotword;
	}
	public void setHotword(String hotword) {
		this.hotword = hotword;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return "InboundQueryData [filename=" + filename + ", filePath=" + filePath + ", hotword=" + hotword
				+ ", timestamp=" + timestamp + "]";
	}
}
