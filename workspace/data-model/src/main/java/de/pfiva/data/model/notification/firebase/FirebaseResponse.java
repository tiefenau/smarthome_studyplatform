package de.pfiva.data.model.notification.firebase;

public class FirebaseResponse {

	private String multicast_id;
	private int success;
	private int failure;
	private String canonical_ids;
	private Result[] results;
	
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public int getFailure() {
		return failure;
	}
	public void setFailure(int failure) {
		this.failure = failure;
	}
	public String getMulticast_id() {
		return multicast_id;
	}
	public void setMulticast_id(String multicast_id) {
		this.multicast_id = multicast_id;
	}
	public String getCanonical_ids() {
		return canonical_ids;
	}
	public void setCanonical_ids(String canonical_ids) {
		this.canonical_ids = canonical_ids;
	}
	public Result[] getResults() {
		return results;
	}
	public void setResults(Result[] results) {
		this.results = results;
	}
}
