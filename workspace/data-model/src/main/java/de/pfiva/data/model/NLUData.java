package de.pfiva.data.model;

import java.io.Serializable;

import de.pfiva.data.model.snips.SnipsOutput;

public class NLUData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private SnipsOutput snipsOutput;
	private Feedback feedback;
	private User user;
	
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "NLUData [snipsOutput=" + snipsOutput + ", feedback=" + feedback + ", user=" + user + "]";
	}
}
