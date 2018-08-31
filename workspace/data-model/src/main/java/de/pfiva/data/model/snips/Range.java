package de.pfiva.data.model.snips;

import java.io.Serializable;

public class Range implements Serializable {

	private static final long serialVersionUID = 1L;
	
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
