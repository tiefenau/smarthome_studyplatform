package de.pfiva.data.model.notification;

public class ClientToken {

	private String clientName;
	private String token;
	
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@Override
	public String toString() {
		return "ClientToken [clientName=" + clientName + ", token=" + token + "]";
	}
}
