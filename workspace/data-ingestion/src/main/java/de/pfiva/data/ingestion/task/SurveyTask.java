package de.pfiva.data.ingestion.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.pfiva.data.ingestion.data.NLUDataIngestionDBService;
import de.pfiva.data.ingestion.service.FirebaseService;
import de.pfiva.data.model.User;
import de.pfiva.data.model.notification.Data.DataType;
import de.pfiva.data.model.notification.SurveyData;
import de.pfiva.data.model.survey.Survey;
import de.pfiva.data.model.survey.Survey.SurveyStatus;

public class SurveyTask implements Runnable {
	
	private FirebaseService firebaseService;
	private NLUDataIngestionDBService dbService;
	private Survey survey;
	private User user;
	
	private static Logger logger = LoggerFactory.getLogger(SurveyTask.class);
	
	public SurveyTask(FirebaseService firebaseService,
			NLUDataIngestionDBService dbService,
			Survey survey, User user) {
		
		this.firebaseService = firebaseService;
		this.dbService = dbService;
		this.survey = survey;
		this.user = user;
	}

	@Override
	public void run() {
		SurveyData data = new SurveyData();
		data.setDatatype(DataType.SURVEY);
		data.setSurvey(this.survey);
		data.setUserId(this.user.getId());
		logger.info("Sending survey [" + this.survey.getSurveyName() + "] to [" + user.getUsername() + "]");
		this.firebaseService.sendRequestToFirebaseServer(data, this.user.getDeviceId());
		
		this.dbService.updateSurveyStatus(survey.getId(), SurveyStatus.DELIVERED);
	}

}
