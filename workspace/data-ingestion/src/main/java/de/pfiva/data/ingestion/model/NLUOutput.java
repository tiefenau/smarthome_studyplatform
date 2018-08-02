//package de.pfiva.data.ingestion.model;
//
//import de.pfiva.data.ingestion.model.snips.InboundQueryData;
//
//public class NLUOutput {
//
//	private InboundQueryData snipsHotword;
//	private InboundQueryData snipsOutput;
//	private Nlu_Completion_Status status = Nlu_Completion_Status.INCOMPLETE;
//	private String hotwordWavFilename;
//	private String hotwordWavFilePath;
//	private String queryWavFilename;
//	private String queryWavFilePath;
//	
//	// This counter is used for determining NLU completion,
//	// as we are processing 4 files, counter should not accessed 4
//	private int filesCounter;
//	
//	public enum Nlu_Completion_Status {
//		INCOMPLETE, PROCESSING, COMPLETE, INVALID;
//	}
//	
//	public InboundQueryData getSnipsHotword() {
//		return snipsHotword;
//	}
//	public void setSnipsHotword(InboundQueryData snipsHotword) {
//		this.snipsHotword = snipsHotword;
//	}
//	public InboundQueryData getSnipsOutput() {
//		return snipsOutput;
//	}
//	public void setSnipsOutput(InboundQueryData snipsOutput) {
//		this.snipsOutput = snipsOutput;
//	}
//	public Nlu_Completion_Status getStatus() {
//		return status;
//	}
//	public void setStatus(Nlu_Completion_Status status) {
//		this.status = status;
//	}
//	public String getHotwordWavFilename() {
//		return hotwordWavFilename;
//	}
//	public void setHotwordWavFilename(String hotwordWavFilename) {
//		this.hotwordWavFilename = hotwordWavFilename;
//	}
//	public String getHotwordWavFilePath() {
//		return hotwordWavFilePath;
//	}
//	public void setHotwordWavFilePath(String hotwordWavFilePath) {
//		this.hotwordWavFilePath = hotwordWavFilePath;
//	}
//	public String getQueryWavFilename() {
//		return queryWavFilename;
//	}
//	public void setQueryWavFilename(String queryWavFilename) {
//		this.queryWavFilename = queryWavFilename;
//	}
//	public String getQueryWavFilePath() {
//		return queryWavFilePath;
//	}
//	public void setQueryWavFilePath(String queryWavFilePath) {
//		this.queryWavFilePath = queryWavFilePath;
//	}
//	public int getFilesCounter() {
//		return filesCounter;
//	}
//	public void setFilesCounter(int filesCounter) {
//		this.filesCounter = filesCounter;
//	}
//	@Override
//	public String toString() {
//		return "NLUOutput [snipsHotword=" + snipsHotword + ", snipsOutput=" + snipsOutput + ", status=" + status
//				+ ", hotwordWavFilename=" + hotwordWavFilename + ", hotwordWavFilePath=" + hotwordWavFilePath
//				+ ", queryWavFilename=" + queryWavFilename + ", queryWavFilePath=" + queryWavFilePath
//				+ ", filesCounter=" + filesCounter + "]";
//	}
//	
//}
