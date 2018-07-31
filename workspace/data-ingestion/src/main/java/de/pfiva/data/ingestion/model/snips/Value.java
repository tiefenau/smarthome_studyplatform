package de.pfiva.data.ingestion.model.snips;

public class Value {

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
