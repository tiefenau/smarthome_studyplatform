package de.pfiva.data.ingestion.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import de.pfiva.data.ingestion.Constants;
import de.pfiva.data.ingestion.DataIngestionProperties;
import de.pfiva.data.ingestion.data.NLUDataIngestionDBService;
import de.pfiva.data.ingestion.model.InputFile;
import de.pfiva.data.ingestion.service.QueryResolverService.UserQueryTuple;
import de.pfiva.data.ingestion.task.MessageTask;
import de.pfiva.data.model.Feedback;
import de.pfiva.data.model.Message;
import de.pfiva.data.model.Message.MessageStatus;
import de.pfiva.data.model.MessageResponseData;
import de.pfiva.data.model.NLUData;
import de.pfiva.data.model.Tuple;
import de.pfiva.data.model.User;
import de.pfiva.data.model.snips.Slot;
import de.pfiva.data.model.snips.SnipsOutput;

@Service
public class NLUDataIngestionService {
	
//	private static final int FILES_COUNTER_THRESHOLD = 4;
//	private static final int VALID_TIME_DIFFERENCE = 5;

	private static Logger logger = LoggerFactory.getLogger(NLUDataIngestionService.class);
	
	@Autowired private NLUDataIngestionDBService dbService;
	@Autowired private FileExtractorService fileExtractor;
	@Autowired private QueryResolverService queryResolverService;
	@Autowired private DataIngestionProperties properties;
	@Autowired private SnipsNLUService nluService;
	@Autowired private FeedbackService feedbackService;
	@Autowired private TaskScheduler taskScheduler;
	@Autowired private FirebaseService firebaseService;
	
	private Map<Integer, ScheduledFuture<?>> messageTasks = new HashMap<>();
//	private NLUOutput nluOutput;
	
	public void extractInboundFileData(InputFile inputFile) {
		SnipsOutput snipsOutput = fileExtractor.extractInboundFileData(inputFile);
		if(snipsOutput != null) {
			snipsOutput.setHotword(properties.getHotword());
			dbService.ingestDataToDB(snipsOutput);
		}
	}
	
	public void extractUserQuery(String userQuery) {
		// User query is plain sentence of some language.
		// This sentence might contain trigger word, which we need to separate out.
		// Once the real query is extracted out, intent classification should be done to populate NLU instance.
		UserQueryTuple queryTuple = queryResolverService.resolveUserQuery(userQuery);
		
		if(queryTuple != null) {
			
			// Do intent classification only if query is present
			if(queryTuple.getQuery() != null) {
				SnipsOutput snipsOutput = nluService.classifyIntents(queryTuple.getQuery());
				
				if(snipsOutput == null) {
					// Cannot classify intents, therefore just save user query
					snipsOutput = new SnipsOutput();
					snipsOutput.setInput(queryTuple.getQuery());
				}
				
				if(queryTuple.getHotword() != null) {
					snipsOutput.setHotword(queryTuple.getHotword());						
				}
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
				snipsOutput.setTimestamp(LocalDateTime.now().format(formatter));
				
				// Push data to db layer
				int queryId = dbService.ingestDataToDB(snipsOutput);
				

				// Send notification
				if(properties.isFeedbackAfterIntentClassification()) {
					feedbackService.sendFeedback(snipsOutput, queryId);						
				}
			}
		}
	}

	public void saveClientRegistrationToken(String clientName, String token) {
		dbService.saveClientRegistrationTokenToDB(clientName, token);
	}

	public List<NLUData> getCompleteNLUData() {
		List<NLUData> nluData = dbService.getQueryIntentFeedbackData();
		
		// NLUData is incomplete without slots. Therefore fetch slots from slots table
		for(NLUData data : nluData) {
			if(data.getSnipsOutput() != null) {
				if(data.getSnipsOutput().getIntent() != null) {
					int intentId = data.getSnipsOutput().getIntent().getIntentId();
					if(intentId != 0) {
						logger.debug("Fetching slots for intent [" + intentId + "].");
						List<Slot> slots = dbService.getSlotsForIntent(intentId);
						if(slots != null && !slots.isEmpty()) {
							data.getSnipsOutput().setSlots(slots);
						}
					}
				}
			}
		}
		
		logger.info("Total user queries fetched [" + nluData.size() + "].");
		return nluData;
	}

	public boolean saveFeedbackResponse(Feedback feedback) {
		int rowsAffected = 0;
		if(feedback != null) {
			rowsAffected = dbService.saveFeedbackResponse(feedback.getId(),
					feedback.getUserResponse(), feedback.getTimestamp());			
		}
		if(rowsAffected == 1) {
			logger.info("Saved user response [" + feedback.getUserResponse() + 
					"] for feedback id [" + feedback.getId() + "]");
			return true;
		} else {
			return false;
		}
	}

	public List<User> getUsers() {
		List<User> users = dbService.getUsers();
		logger.info("Users fetched from db " + users);
		return users;
	}

	public void sendMessage(Message message) {
		// 1. Save message to db
		// 2. Based on delivery date either send message or schedule a message to be sent later
		if(message != null) {
			// Delivery date would either be Now or an actual timestamp
			String deliveryDate = message.getDeliveryDateTime();
			if(deliveryDate != null && !deliveryDate.trim().isEmpty()) {
				if(deliveryDate.equalsIgnoreCase("now")) {
					logger.info("Message is scheduled to be delivered now.");
					message.setMessageStatus(MessageStatus.DELIVERED);
					
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
					message.setDeliveryDateTime(LocalDateTime.now().format(formatter));
				} else {
					logger.info("Message is scheduled to be delivered later.");
					message.setMessageStatus(MessageStatus.PENDING);
				}
				
				Tuple<Integer, Boolean> status = dbService.saveMessageToDB(message);
//				boolean status = true;
				if(status.getY()) {
					message.setId(status.getX());
					processMessageForDelivery(message);
				}
			}
		}
	}
	
	private void processMessageForDelivery(Message message) {
		DateFormat formatter = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
		Date date = null;
		try {
			date = formatter.parse(message.getDeliveryDateTime());
		} catch (ParseException e) {
			
		}
		
		logger.info("Message scheduled to be delivered to [" + message.getUsers().size()
				+ "] users.");
		for(User user : message.getUsers()) {
			scheduleMessage(message.getId(), message.getMessageText(), 
					date, user);
		}
	}

	@Async
	private void scheduleMessage(int id, String messageText, Date date, User user) {
		ScheduledFuture<?> schedule = taskScheduler.schedule(
				new MessageTask(firebaseService, dbService, id, messageText, user), date);
		
		messageTasks.put(id, schedule);
	}
	
	public boolean cancelScheduledMessage(int messageId) {
		if(!messageTasks.isEmpty()) {
			ScheduledFuture<?> scheduledTask = messageTasks.get(messageId);
			if(scheduledTask != null) {
				scheduledTask.cancel(true);
				
				if(scheduledTask.isCancelled()) {
					logger.info("Cancelled scheduled task for message id [" + messageId + "]");
					messageTasks.remove(messageId);
					
					dbService.updateMessageStatus(messageId, MessageStatus.CANCELLED);
					return true;
				}
			}
		}
		return false;
	}

	public List<MessageResponseData> getMessageResponseData() {
		// 1. Fetch messages from db
		// 2. Fetch all users for a message from db
		// 3. Fetch response from users
		List<MessageResponseData> response = new LinkedList<>();
		
		List<Message> messages = dbService.getMessages();
		for(Message message : messages) {
			List<User> users = dbService.getUsersByMessageId(message.getId());
			message.setUsers(users);
			
			MessageResponseData messageResponseData = new MessageResponseData();
			messageResponseData.setMessage(message);
			response.add(messageResponseData);
		}
		logger.info("Fetched message response data");
		return response;
	}

	public boolean addNewUser(User user) {
		return dbService.addNewUser(user);
	}

	public boolean deleteUser(int userId) {
		return dbService.deleteUser(userId);
	}
	
	// On receiving data, check for completion, if data
	// is complete push to database.
	/*private void ingestData(InboundQueryData inboundFileData) {
		
		createNluInstance();
		
		populateNluInstance(inboundFileData);
		
		checkForCompletness();
		
		if(nluOutput.getStatus() == Nlu_Completion_Status.COMPLETE) {
//			dbService.ingestDataToDB(nluOutput);
		}
		
		destroyNluInstance();
	}

	private void createNluInstance() {
		if(nluOutput == null) {
			nluOutput = new NLUOutput();
			nluOutput.setFilesCounter(1); 
			logger.info("New instance created for [NLUOutput].");
		} else {
			int count = nluOutput.getFilesCounter();
			nluOutput.setFilesCounter(count+1);
		}
		nluOutput.setStatus(Nlu_Completion_Status.PROCESSING);
	}
	
	private void populateNluInstance(InboundQueryData inboundFileData) {
		if(inboundFileData instanceof SnipsHotword) {
			nluOutput.setSnipsHotword(inboundFileData);
		} else if(inboundFileData instanceof SnipsOutput) {
			nluOutput.setSnipsOutput(inboundFileData);
		} else {
			populateWavFileDetails(inboundFileData, nluOutput);
		}
	}
	
	private void populateWavFileDetails(InboundQueryData inboundFileData, NLUOutput nluOutput) {
		if(inboundFileData.getFilename().matches(Constants.HOTWORD_WAV_REGX)) {
			nluOutput.setHotwordWavFilename(inboundFileData.getFilename());
			nluOutput.setHotwordWavFilePath(inboundFileData.getFilePath());
		} else if(inboundFileData.getFilename().matches(Constants.QUERY_WAV_REGX)) {
			nluOutput.setQueryWavFilename(inboundFileData.getFilename());
			nluOutput.setQueryWavFilePath(inboundFileData.getFilePath());
		}
	}
	
	private void checkForCompletness() {
		// This counter is used for determining NLU completion,
		// as we are processing 4 files, counter should not accessed 4
		if(nluOutput.getFilesCounter() == FILES_COUNTER_THRESHOLD) {
			if(nluOutput.getSnipsHotword() != null
					&& nluOutput.getSnipsOutput() != null) {

				if(isDataValid(nluOutput.getSnipsHotword(), nluOutput.getSnipsOutput())) {
					logger.info("NLUOutput status [" + Nlu_Completion_Status.COMPLETE + "]");
					nluOutput.setStatus(Nlu_Completion_Status.COMPLETE);
				} else {
					logger.info("NLUOutput status [" + Nlu_Completion_Status.INVALID + "]");
					nluOutput.setStatus(Nlu_Completion_Status.INVALID);
				}
			}
		} else if(nluOutput.getFilesCounter() > FILES_COUNTER_THRESHOLD) {
			logger.info("Files counter exceeded threshold,"
					+ " setting NLUOutput status [" + Nlu_Completion_Status.INVALID + "]");
			nluOutput.setStatus(Nlu_Completion_Status.INVALID);
		}
	}
	
	private boolean isDataValid(InboundQueryData snipsHotword, InboundQueryData snipsOutput) {
		// Check - timestamp between hotword and query is not too much
		// this is to make sure correctness of data
		
		long diffInMillies = snipsHotword.getTimestamp().getTime()
				- snipsOutput.getTimestamp().getTime();
		long seconds = TimeUnit.MILLISECONDS.toSeconds(diffInMillies);
		logger.info("Time difference for NLUOutput completion status is [" + seconds + " seconds].");
		if(seconds > VALID_TIME_DIFFERENCE) {
			return false;
		}
		return true;
	}
	
	private void destroyNluInstance() {
		if(nluOutput.getStatus() == Nlu_Completion_Status.COMPLETE
				|| nluOutput.getStatus() == Nlu_Completion_Status.INVALID) {
			logger.info("NLUOutput status [" + nluOutput.getStatus() + "],"
					+ " destroying NLUOutput instance.");
			nluOutput = null;
		}
	}*/
}
