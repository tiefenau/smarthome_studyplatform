package de.pfiva.data.model.message;

import java.io.Serializable;

import de.pfiva.data.model.User;

public class Response implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String value;
	private User user;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString() {
		return "Response [id=" + id + ", value=" + value + ", user=" + user + "]";
	}
}
