package de.pfiva.data.model.snips;

import java.io.Serializable;

public class Value implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String kind;
	private String value;
	private String grain;
	private String precision;
	
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getGrain() {
		return grain;
	}
	public void setGrain(String grain) {
		this.grain = grain;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}
	@Override
	public String toString() {
		return "Value [kind=" + kind + ", value=" + value + ", grain=" + grain + ", precision=" + precision + "]";
	}
}
