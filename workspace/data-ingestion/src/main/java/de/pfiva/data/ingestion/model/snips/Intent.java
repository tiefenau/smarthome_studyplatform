package de.pfiva.data.ingestion.model.snips;

import java.math.BigDecimal;

public class Intent {

	private int intentId;
	private String intentName;
	private BigDecimal probability;
	
	public int getIntentId() {
		return intentId;
	}
	public void setIntentId(int intentId) {
		this.intentId = intentId;
	}
	public String getIntentName() {
		return intentName;
	}
	public void setIntentName(String intentName) {
		this.intentName = intentName;
	}
	public BigDecimal getProbability() {
		return probability;
	}
	public void setProbability(BigDecimal probability) {
		this.probability = probability;
	}
	
	@Override
	public String toString() {
		return "Intent [intentId=" + intentId + ", intentName=" + intentName + ", probability=" + probability + "]";
	}
}
