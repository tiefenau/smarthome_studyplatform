package de.pfiva.data.model.snips;

public class Range {

	private int start;
	private int end;
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
	@Override
	public String toString() {
		return "Range [start=" + start + ", end=" + end + "]";
	}
}
