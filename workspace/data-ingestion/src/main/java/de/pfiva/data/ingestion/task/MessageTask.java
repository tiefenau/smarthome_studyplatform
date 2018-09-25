package de.pfiva.data.ingestion.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.pfiva.data.ingestion.data.NLUDataIngestionDBService;
import de.pfiva.data.ingestion.service.FirebaseService;
import de.pfiva.data.model.User;
import de.pfiva.data.model.message.Message.MessageStatus;
import de.pfiva.data.model.notification.Data.DataType;
import de.pfiva.data.model.notification.MessageData;

public class MessageTask implements Runnable {
	private int id;
	private String messageText;
	private User user;
	private FirebaseService firebaseService;
	private NLUDataIngestionDBService dbService;
	
	private static Logger logger = LoggerFactory.getLogger(MessageTask.class);
	
	public MessageTask(FirebaseService firebaseService,
			NLUDataIngestionDBService dbService,
			int id, String messageText, User user) {
		this.firebaseService = firebaseService;
		this.dbService = dbService;
		this.id = id;
		this.messageText = messageText;
		this.user = user;
	}

	@Override
	public void run() {
		MessageData data = new MessageData();
		data.setDatatype(DataType.MESSAGE);
		data.setMessageId(this.id);
		data.setMessageText(this.messageText);
		data.setUserId(this.user.getId());
		logger.info("Sending message with id [" + this.id + "] to [" + user.getUsername() + "]");
		this.firebaseService.sendRequestToFirebaseServer(data, this.user.getDeviceId());
		
		this.dbService.updateMessageStatus(id, MessageStatus.DELIVERED);
	}
}
