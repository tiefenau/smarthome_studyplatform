package de.pfiva.data.ingestion.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class QueryResolverService {

	private static Logger logger = LoggerFactory.getLogger(QueryResolverService.class);
	
	public UserQueryTuple resolveUserQuery(String userQuery) {
		if(userQuery != null && !userQuery.isEmpty()) {
			logger.info("Resolving user query [" + userQuery + "]");
			return splitHotwordAndUserQuery(userQuery);
		}
		return null;
	}
	
	private UserQueryTuple splitHotwordAndUserQuery(String userQuery) {
		// 1. Hotword can be present in starting of the sentence
		// 2. Hotword present in middle of a sentence
		// 3. Hotword present in end of a sentence
		// we only have to check if hotword is present in starting of user query
		UserQueryTuple queryTuple = new UserQueryTuple();
		
		// Hotword extraction
		for(Hotwords hotword : Hotwords.values()) {
			String regx = hotword.getHotwordString() + ".*";
			if(userQuery.matches(regx)) {
				queryTuple.setHotword(hotword.getHotwordString());
				break;
			}
		}
		
		// Query extraction
		if(queryTuple.getHotword() != null) {
			// length of hotward plus one space
			String query = userQuery.substring((queryTuple.getHotword().length()+1));
			queryTuple.setQuery(query);
		} else {
			// no hotword in userquery, thus use the whole query text
			queryTuple.setQuery(userQuery);
		}
		logger.info("Hotword [" + queryTuple.getHotword() + "], "
				+ "User query [" + queryTuple.getQuery() + "] extracted.");
		
		return queryTuple;
	}
	
	enum Hotwords {
		OK_GOOGLE("Ok Google"),
		SIRI("Siri"),
		ALEXA("Alexa");
		
		private String hotword;
		
		Hotwords(String hotword) {
			this.hotword = hotword;
		}

		public String getHotwordString() {
			return hotword;
		}
	}

	class UserQueryTuple {
		private String hotword;
		private String query;
		
		public String getHotword() {
			return hotword;
		}
		public void setHotword(String hotword) {
			this.hotword = hotword;
		}
		public String getQuery() {
			return query;
		}
		public void setQuery(String query) {
			this.query = query;
		}
		@Override
		public String toString() {
			return "UserQueryTuple [hotword=" + hotword + ", query=" + query + "]";
		}
	}
}
