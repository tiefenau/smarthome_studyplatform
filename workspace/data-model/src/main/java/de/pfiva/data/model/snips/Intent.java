package de.pfiva.data.model.snips;

import java.io.Serializable;
import java.math.BigDecimal;

public class Intent implements Serializable {

	private static final long serialVersionUID = 1L;
	
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
