package de.pfiva.data.model.notification;

import java.io.Serializable;

public class MessageData extends Data implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int messageId;
	private String messageText;
	private int userId;
	
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "MessageData [messageId=" + messageId + ", messageText=" + messageText + ", userId=" + userId + "]";
	}
}
