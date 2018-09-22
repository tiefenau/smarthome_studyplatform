package de.pfiva.data.model.survey;

import java.io.Serializable;

public class Option implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String value;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "Option [id=" + id + ", value=" + value + "]";
	}
}
