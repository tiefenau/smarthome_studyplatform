package de.pfiva.data.model.snips;

public class Slot {

	private Range range;
	private String rawValue;
	private Value value;
	private String entity;
	private String slotName;
	
	public Range getRange() {
		return range;
	}
	public void setRange(Range range) {
		this.range = range;
	}
	public String getRawValue() {
		return rawValue;
	}
	public void setRawValue(String rawValue) {
		this.rawValue = rawValue;
	}
	public Value getValue() {
		return value;
	}
	public void setValue(Value value) {
		this.value = value;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public String getSlotName() {
		return slotName;
	}
	public void setSlotName(String slotName) {
		this.slotName = slotName;
	}
	@Override
	public String toString() {
		return "Slot [range=" + range + ", rawValue=" + rawValue + ", value=" + value + ", entity=" + entity
				+ ", slotName=" + slotName + "]";
	}
}
