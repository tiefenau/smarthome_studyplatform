package de.pfiva.data.model;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String username;
	private String deviceId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", deviceId=" + deviceId + "]";
	}
}
