package de.pfiva.data.ingestion.model.snips;

import java.util.List;

public class SnipsOutput extends InboundQueryData {

	private String id;
	private String input;
	private Intent intent;
	private List<Slot> slots;
	private String sessionId;
	
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
	@Override
	public String toString() {
		return "SnipsOutput [id=" + id + ", input=" + input + ", intent=" + intent + ", slots=" + slots + ", sessionId="
				+ sessionId + ", toString()=" + super.toString() + "]";
	}
}
