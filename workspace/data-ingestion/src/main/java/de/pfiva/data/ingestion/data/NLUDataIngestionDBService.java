package de.pfiva.data.ingestion.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.pfiva.data.ingestion.model.NLUOutput;
import de.pfiva.data.ingestion.model.snips.InboundQueryData;

@Service
public class NLUDataIngestionDBService {

	private static Logger logger = LoggerFactory.getLogger(NLUDataIngestionDBService.class);
	
	public void ingestDataToDB(InboundQueryData inboundQueryData) {
		logger.info("Inside db layer..." + inboundQueryData);
	}

}
