package de.pfiva.data.model.notification;

import java.io.Serializable;

public class MessageData extends Data implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int messageId;
	private String messageText;
	
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
	@Override
	public String toString() {
		return "MessageData [messageId=" + messageId + ", messageText=" + messageText + "]";
	}
}
