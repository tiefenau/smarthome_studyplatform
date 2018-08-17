package de.pfiva.data.model.snips;

import java.util.List;

public class SnipsOutput {

	private String id;
	private String input;
	private Intent intent;
	private List<Slot> slots;
	private String sessionId;
	
	private String filename;
	private String filePath;
	private String hotword;
	private String timestamp;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public Intent getIntent() {
		return intent;
	}
	public void setIntent(Intent intent) {
		this.intent = intent;
	}
	public List<Slot> getSlots() {
		return slots;
	}
	public void setSlots(List<Slot> slots) {
		this.slots = slots;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
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
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return "SnipsOutput [id=" + id + ", input=" + input + ", intent=" + intent + ", slots=" + slots + ", sessionId="
				+ sessionId + ", filename=" + filename + ", filePath=" + filePath + ", hotword=" + hotword
				+ ", timestamp=" + timestamp + "]";
	}
}
