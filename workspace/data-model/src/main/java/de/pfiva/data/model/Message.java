package de.pfiva.data.model;

import java.io.Serializable;
import java.util.List;

public class Message  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String messageText;
	private String deliveryDateTime;
	private MessageStatus messageStatus;
	private List<User> users;
	
	public enum MessageStatus {
		DELIVERED, PENDING, CANCELLED
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public String getDeliveryDateTime() {
		return deliveryDateTime;
	}
	public void setDeliveryDateTime(String deliveryDateTime) {
		this.deliveryDateTime = deliveryDateTime;
	}
	public MessageStatus getMessageStatus() {
		return messageStatus;
	}
	public void setMessageStatus(MessageStatus messageStatus) {
		this.messageStatus = messageStatus;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	@Override
	public String toString() {
		return "Message [id=" + id + ", messageText=" + messageText + ", deliveryDateTime=" + deliveryDateTime
				+ ", messageStatus=" + messageStatus + ", users=" + users + "]";
	}
}
