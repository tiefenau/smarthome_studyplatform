package de.pfiva.data.model.notification;

import java.io.Serializable;

public class Data implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public enum DataType {
		FEEDBACK, MESSAGE, SURVEY;
	}
	
	private DataType datatype;
	
	public DataType getDatatype() {
		return datatype;
	}

	public void setDatatype(DataType datatype) {
		this.datatype = datatype;
	}
}
