package de.pfiva.data.model;

import de.pfiva.data.model.snips.SnipsOutput;

public class NLUData {

	private SnipsOutput snipsOutput;
	private Feedback feedback;
	
	public SnipsOutput getSnipsOutput() {
		return snipsOutput;
	}
	public void setSnipsOutput(SnipsOutput snipsOutput) {
		this.snipsOutput = snipsOutput;
	}
	public Feedback getFeedback() {
		return feedback;
	}
	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}
	@Override
	public String toString() {
		return "NLUData [snipsOutput=" + snipsOutput + ", feedback=" + feedback + "]";
	}
}
