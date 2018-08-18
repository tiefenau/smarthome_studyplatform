package de.pfiva.data.model.notification;

public class RequestModel {

	private Data data;
	private String to;
	
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	@Override
	public String toString() {
		return "RequestModel [data=" + data + ", to=" + to + "]";
	}
}
