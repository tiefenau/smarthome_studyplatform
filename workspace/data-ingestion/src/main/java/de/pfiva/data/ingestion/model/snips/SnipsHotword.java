package de.pfiva.data.ingestion.model.snips;

public class SnipsHotword extends InboundQueryData {

	private String siteId;
	private String modelId;
	private String modelVersion;
	private String modelType;
	private float currentSensitivity;
	
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getModelId() {
		return modelId;
	}
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	public String getModelVersion() {
		return modelVersion;
	}
	public void setModelVersion(String modelVersion) {
		this.modelVersion = modelVersion;
	}
	public String getModelType() {
		return modelType;
	}
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
	public float getCurrentSensitivity() {
		return currentSensitivity;
	}
	public void setCurrentSensitivity(float currentSensitivity) {
		this.currentSensitivity = currentSensitivity;
	}
	@Override
	public String toString() {
		return "SnipsHotword [siteId=" + siteId + ", modelId=" + modelId + ", modelVersion=" + modelVersion
				+ ", modelType=" + modelType + ", currentSensitivity=" + currentSensitivity + "]";
	}
}
