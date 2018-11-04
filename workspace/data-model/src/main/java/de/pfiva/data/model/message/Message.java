package de.pfiva.data.model.message;

import java.io.Serializable;
import java.util.List;

import de.pfiva.data.model.User;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String messageText;
	private String deliveryDateTime;
	private MessageStatus messageStatus;
	private String topic;
	private List<User> users;
	
	public enum MessageStatus {
		DELIVERED, PENDING, CANCELLED, FAILED
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
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
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
				+ ", messageStatus=" + messageStatus + ", topic=" + topic + ", users=" + users + "]";
	}
}
