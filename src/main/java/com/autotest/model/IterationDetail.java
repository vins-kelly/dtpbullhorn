package com.autotest.model;

import java.util.List;

public class IterationDetail {

	private int id;
	private String outcome;
	private String errorMessage;
	private List<ActionResult> actionResults;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<ActionResult> getActionResults() {
		return actionResults;
	}

	public void setActionResults(List<ActionResult> actionResults) {
		this.actionResults = actionResults;
	}

}
