package de.pfiva.data.model.message;

import java.util.List;

public class MessageResponseData {

	private Message message;
	private List<Response> responses;

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public List<Response> getResponses() {
		return responses;
	}

	public void setResponses(List<Response> responses) {
		this.responses = responses;
	}

	@Override
	public String toString() {
		return "MessageResponseData [message=" + message + ", responses=" + responses + "]";
	}
}
