package de.pfiva.data.model.survey;

import java.io.Serializable;
import java.util.List;

import de.pfiva.data.model.User;

public class Response implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private int questionId;
	private List<String> values;
	private User user;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Response [id=" + id + ", questionId=" + questionId + ", values=" + values + ", user=" + user + "]";
	}
}
