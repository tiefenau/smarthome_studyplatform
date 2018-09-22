package de.pfiva.data.model.survey;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String question;
	private String questionType;
	private List<Option> options;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}
	@Override
	public String toString() {
		return "Question [id=" + id + ", question=" + question + ", questionType=" + questionType + ", options="
				+ options + "]";
	}
}
