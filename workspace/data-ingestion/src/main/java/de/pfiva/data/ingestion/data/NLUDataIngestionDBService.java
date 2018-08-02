package de.pfiva.data.ingestion.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import de.pfiva.data.ingestion.Constants;
import de.pfiva.data.ingestion.model.snips.Intent;
import de.pfiva.data.ingestion.model.snips.Slot;
import de.pfiva.data.ingestion.model.snips.SnipsOutput;

@Service
public class NLUDataIngestionDBService {

	private static Logger logger = LoggerFactory.getLogger(NLUDataIngestionDBService.class);
	
	@Autowired JdbcTemplate jdbcTemplate;
	
	public boolean ingestDataToDB(SnipsOutput snipsOutput) {
		if(snipsOutput == null) {
			logger.info("Nothing to push in database.");
			return false;
		}
		
		// Insert in query_tbl
		int queryTableRowId = insertIntoQueryTable(snipsOutput);
		
		
		// Insert in intent_tbl
		if(snipsOutput.getIntent() != null) {
			int intentTableRowId = insertIntoIntentTable(snipsOutput.getIntent(), queryTableRowId);
			
			// Insert in slots_tbl
			if(snipsOutput.getSlots() != null) {
				insertIntoSlotsTable(snipsOutput.getSlots(), intentTableRowId);
			}
		}
		
		logger.info("Insertion in databse successful.");
		return true;
	}

	private int insertIntoQueryTable(SnipsOutput snipsOutput) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con
						.prepareStatement(DataIngestionDBQueries.INSERT_QUERY_TBL, new String[] {"query_id"});
				
				ps.setString(1, snipsOutput.getInput());
				ps.setString(2, snipsOutput.getHotword());
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_FORMAT);
				String currentDateTimeString = snipsOutput.getTimestamp().format(formatter);
				ps.setString(3, currentDateTimeString);
				
				String completeFileLocation = null;
				if(snipsOutput.getFilename() != null && snipsOutput.getFilePath() != null) {
					completeFileLocation = snipsOutput.getFilePath()
							+ snipsOutput.getFilename();
					
				}
				ps.setString(4, completeFileLocation);
				
				return ps;
			}
		}, keyHolder);
		
		int rowId = keyHolder.getKey().intValue();
		logger.info("Inserted record in query_tbl with row id [" + rowId + "]");
		return rowId;
	}
	
	private int insertIntoIntentTable(Intent intent, int queryTableRowId) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con
						.prepareStatement(DataIngestionDBQueries.INSERT_INTENT_TBL, new String[] {"intent_id"});
				
				ps.setString(1, intent.getIntentName());
				ps.setInt(2, queryTableRowId);
				
				return ps;
			}
		}, keyHolder);
		
		int rowId = keyHolder.getKey().intValue();
		logger.info("Inserted record in intent_tbl with row id [" + rowId + "]");
		return rowId;
	}
	
	private void insertIntoSlotsTable(List<Slot> slots, int intentTableRowId) {
		jdbcTemplate.batchUpdate(DataIngestionDBQueries.INSERT_SLOTS_TBL, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, slots.get(i).getSlotName());
				ps.setString(2, slots.get(i).getRawValue());
				ps.setInt(3, intentTableRowId);
			}
			
			@Override
			public int getBatchSize() {
				return slots.size();
			}
		});
		
		logger.info("Inserted [" + slots.size() + "] records in slots_tbl");
	}

}
