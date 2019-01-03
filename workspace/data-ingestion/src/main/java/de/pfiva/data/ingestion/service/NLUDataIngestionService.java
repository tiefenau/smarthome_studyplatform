package de.pfiva.data.ingestion.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import de.pfiva.data.ingestion.Constants;
import de.pfiva.data.ingestion.data.NLUDataIngestionDBService;
import de.pfiva.data.ingestion.service.QueryResolverService.UserQueryTuple;
import de.pfiva.data.ingestion.task.MessageTask;
import de.pfiva.data.ingestion.task.SurveyTask;
import de.pfiva.data.model.Feedback;
import de.pfiva.data.model.NLUData;
import de.pfiva.data.model.PfivaConfigData;
import de.pfiva.data.model.Topic;
import de.pfiva.data.model.Tuple;
import de.pfiva.data.model.User;
import de.pfiva.data.model.message.Message;
import de.pfiva.data.model.message.Message.MessageStatus;
import de.pfiva.data.model.message.MessageResponseData;
import de.pfiva.data.model.message.Response;
import de.pfiva.data.model.snips.Slot;
import de.pfiva.data.model.snips.SnipsOutput;
import de.pfiva.data.model.survey.Option;
import de.pfiva.data.model.survey.Question;
import de.pfiva.data.model.survey.Survey;
import de.pfiva.data.model.survey.Survey.SurveyStatus;
import de.pfiva.data.model.survey.SurveyResponseData;

@Service
public class NLUDataIngestionService {
	
	private static Logger logger = LoggerFactory.getLogger(NLUDataIngestionService.class);
	
	@Autowired private NLUDataIngestionDBService dbService;
	@Autowired private QueryResolverService queryResolverService;
	@Autowired private SnipsNLUService nluService;
	@Autowired private FeedbackService feedbackService;
	@Autowired private TaskScheduler taskScheduler;
	@Autowired private FirebaseService firebaseService;
	@Autowired private GoogleCloudService googleCloudService;
	
	private Map<Integer, ScheduledFuture<?>> messageTasks = new HashMap<>();
	private Map<Integer, ScheduledFuture<?>> surveyTasks = new HashMap<>();
	
	public String getSpeechToText(MultipartFile file, String language) {
		return googleCloudService.getSpeechToText(file, language);
	}
	
	public void extractUserQuery(String userQuery, String username) {
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

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
				snipsOutput.setTimestamp(LocalDateTime.now().format(formatter));
				
				int userId = getUserId(username);
				
				if(queryTuple.getHotword() != null) {
					snipsOutput.setHotword(queryTuple.getHotword());
					// Push data to db layer
					
					int queryId = dbService.ingestDataToDB(snipsOutput, userId);


					// Send notification
					PfivaConfigData configurationValue = dbService
							.getConfigurationValue(Constants.PFIVA_INSTANT_FEEDBACK);
					if(Boolean.valueOf(configurationValue.getValue())) {
						feedbackService.sendFeedback(snipsOutput, queryId, username);						
					}
				} else {
					// Capture queries without hotword
					PfivaConfigData configurationValue = dbService
							.getConfigurationValue(Constants.PFIVA_CAPTURE_QUERIES_WITHOUT_HOTWORD);
					if(Boolean.valueOf(configurationValue.getValue())) {
						
						// Push data to db layer
						int queryId = dbService.ingestDataToDB(snipsOutput, userId);


						// Send notification
						PfivaConfigData configData = dbService
								.getConfigurationValue(Constants.PFIVA_INSTANT_FEEDBACK);
						if(Boolean.valueOf(configData.getValue())) {
							feedbackService.sendFeedback(snipsOutput, queryId, username);						
						}
					}
				}
			}				
		}
	}
	
	private int getUserId(String username) {
		int userId = 0;
		if(username != null && !username.isEmpty()) {
			userId = dbService.getUserId(username);			
		}
		return userId;
	}

	public void saveClientRegistrationToken(String clientName, String token) {
		dbService.saveClientRegistrationTokenToDB(clientName, token);
	}

	public List<NLUData> getCompleteNLUData() {
		List<NLUData> nluData = dbService.getQueryIntentFeedbackData(0);
		
		// NLUData is incomplete without slots. Therefore fetch slots from slots table
		fetchSlotsData(nluData);
		
		logger.info("Total user queries fetched [" + nluData.size() + "].");
		return nluData;
	}
	
	public List<NLUData> getCompleteNLUDataByUser(String deviceId) {
		int userId = dbService.getUserByDeviceId(deviceId);
		List<NLUData> nluData = dbService.getQueryIntentFeedbackData(userId);
		
		// NLUData is incomplete without slots. Therefore fetch slots from slots table
		fetchSlotsData(nluData);
		
		logger.info("Total user queries fetched [" + nluData.size() + "].");
		return nluData;
	}
	
	private void fetchSlotsData(List<NLUData> nluData) {
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
			int topicId = createTopicIfNotExists(message.getTopic());
			
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
				
				Tuple<Integer, Boolean> status = dbService.saveMessageToDB(message, topicId);
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

	public List<Message> getMessages() {
		// 1. Fetch messages from db
		// 2. Fetch all users for a message from db
		// 3. Fetch response from users
		List<Message> messages = dbService.getMessages();
		for(Message message : messages) {
			List<User> users = dbService.getUsersByMessageId(message.getId());
			message.setUsers(users);
		}
		return messages;
	}
	
	public List<Message> getMessagesByTopic(String topic) {
		List<Message> messages = dbService.getMessagesByTopic(topic);
		for(Message message : messages) {
			List<User> users = dbService.getUsersByMessageId(message.getId());
			message.setUsers(users);
		}
		return messages;
	}
	
	public MessageResponseData getCompleteMessageData(int messageId) {
		// 1. Fetch message from db by messageId
		// 2. Fetch all users for a message from db
		// 3. Fetch response from users
		MessageResponseData response = new MessageResponseData();
		Message message = dbService.getMessage(messageId);
		
		List<User> users = dbService.getUsersByMessageId(messageId);
		message.setUsers(users);
		
		response.setMessage(message);
		
		List<Response> responses = dbService.getMessageResponses(messageId);
		response.setResponses(responses);
		return response;
	}

	public boolean addNewUser(User user) {
		return dbService.addNewUser(user);
	}

	public boolean deleteUser(int userId) {
		return dbService.deleteUser(userId);
	}

	public List<PfivaConfigData> getConfigurationValues() {
		return dbService.getConfigurationValues();
	}

	public void saveConfigValue(PfivaConfigData configData) {
		if(configData != null) {
			dbService.saveConfigValue(configData);
		}
	}

	public void sendSurvey(Survey survey) {
		// 1. Save survey to db
		// 2. Based on delivery date either send survey or schedule a survey to be sent later
		if(survey != null) {
			// First check if topic exists, it not then create new other wise update date
			int topicId = createTopicIfNotExists(survey.getTopic());
			
			// Delivery date would either be Now or an actual timestamp
			String deliveryDate = survey.getDeliveryDateTime();
			if(deliveryDate != null && !deliveryDate.trim().isEmpty()) {
				if(deliveryDate.equalsIgnoreCase("now")) {
					logger.info("Survey is scheduled to be delivered now.");
					survey.setSurveyStatus(SurveyStatus.DELIVERED);

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
					survey.setDeliveryDateTime(LocalDateTime.now().format(formatter));
				} else {
					logger.info("Survey is scheduled to be delivered later.");
					survey.setSurveyStatus(SurveyStatus.PENDING);
				}

				Tuple<Integer, Boolean> status = dbService.saveSurveyToDB(survey, topicId);
				
				if(status.getY()) {
					// Get updated data for survey from db
					Survey surveyData = getSurveyData(status.getX());
					processSurveyForDelivery(surveyData);
				}
			}
		}
	}
	
	private int createTopicIfNotExists(String topic) {
		int topicId = 0;
		if(topic != null && !topic.isEmpty()) {
			topicId = dbService.getTopicId(topic);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
			String date = LocalDateTime.now().format(formatter);
			if(topicId != 0) {
				logger.info("Topic [" + topic + "] exists.");
				dbService.updateTopicModificationDate(topicId, date);
			} else {
				logger.info("Topic [" + topic + "] does not exists.");
				topicId = dbService.createNewTopic(topic, date);
			}
		}
		return topicId;
	}

	private void processSurveyForDelivery(Survey survey) {
		DateFormat formatter = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
		Date date = null;
		try {
			date = formatter.parse(survey.getDeliveryDateTime());
		} catch (ParseException e) {
			
		}
		
		logger.info("Survey scheduled to be delivered to [" + survey.getUsers().size()
				+ "] users.");
		for(User user : survey.getUsers()) {
			scheduleSurvey(survey, date, user);
		}
	}

	@Async
	private void scheduleSurvey(Survey survey, Date date, User user) {
		ScheduledFuture<?> schedule = taskScheduler.schedule(
				new SurveyTask(firebaseService, dbService, survey, user), date);
		
		surveyTasks.put(survey.getId(), schedule);
	}
	
	public boolean cancelScheduledSurvey(int surveyId) {
		if(!surveyTasks.isEmpty()) {
			ScheduledFuture<?> scheduledTask = surveyTasks.get(surveyId);
			if(scheduledTask != null) {
				scheduledTask.cancel(true);
				
				if(scheduledTask.isCancelled()) {
					logger.info("Cancelled scheduled task for survey id [" + surveyId + "]");
					surveyTasks.remove(surveyId);
					
					dbService.updateSurveyStatus(surveyId, SurveyStatus.CANCELLED);
					return true;
				}
			}
		}
		return false;
	}

	public List<Survey> getSurveys() {
		return dbService.getSurveys();
	}
	
	public List<Survey> getSurveysByTopic(String topic) {
		return dbService.getSurveysByTopic(topic);
	}
	
	public SurveyResponseData getCompleteSurveyData(int surveyId) {
		SurveyResponseData data = new SurveyResponseData();
		data.setSurvey(getSurveyData(surveyId));
		data.setResponses(getSurveyResponse(surveyId));
		return data;
	}
	
	private Set<de.pfiva.data.model.survey.Response> getSurveyResponse(int surveyId) {
		List<de.pfiva.data.model.survey.Response> responses = dbService.getSurveyResponse(surveyId);
		
		// Remove duplicate responses
		Set<de.pfiva.data.model.survey.Response> responseSet = new HashSet<>(responses);
		
		return responseSet;
	}

	private Survey getSurveyData(int surveyId) {
		// 1. Get survey data
		// 2. Get users per survey
		// 3. Get questions data per survey
		Survey survey = dbService.getSurveyById(surveyId);
		
		List<User> users = dbService.getUsersBySurveyId(survey.getId());
		survey.setUsers(users);

		List<Question> questions = dbService.getQuestionsBySurveyId(survey.getId());
		
		for(Question question : questions) {
			List<Option> options = dbService.getOptionsForQuestion(question.getId());
			question.setOptions(options);
		}
		survey.setQuestions(questions);
		return survey;
	}

	public boolean saveMessageResponse(int messageId, Response response) {
		int rowsAffected = 0;
		if(response != null) {
			rowsAffected = dbService.saveMessageResponse(messageId, response);			
		}
		if(rowsAffected == 1) {
			logger.info("Saved user response [" + response.getValue() + 
					"] for message id [" + messageId + "]");
			return true;
		} else {
			return false;
		}
	}

	public boolean saveSurveyResponse(int surveyId, 
			List<de.pfiva.data.model.survey.Response> responses) {
		
		return dbService.saveSurveyResponse(surveyId, responses);
	}

	public List<Topic> getTopics() {
		// 1. Get topic data from topic_tbl
		// 2. Fetch survey count
		// 3. Fetch message count
		
		List<Topic> topics = dbService.getTopics();
		for(Topic topic : topics) {
			int surveyCount = dbService.getSurveyCount(topic.getId());
			topic.setSurveyCount(surveyCount);
			
			int messageCount = dbService.getMessageCount(topic.getId());
			topic.setMessageCount(messageCount);
		}
		return topics;
	}

	public List<String> getTopicNames() {
		return dbService.getTopicNames();
	}

	public boolean deleteTopicWithAssociatedData(int topicId) {
		// 1. Delete surveys from survey_tbl
		// 2. Delete messages from messages_tbl
		// 3. Delete topic from topic_tbl
		
		boolean status = dbService.deleteSurveys(topicId);
		status = dbService.deleteMessages(topicId);
		status = dbService.deleteTopic(topicId);
		return status;
	}
}
