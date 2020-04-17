package com.autotest.model;

import java.util.List;

public class WriteResult {

	private long id;
	private String state;
	private String outcome;
	private List<IterationDetail> iterationDetails;
	private String url;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public List<IterationDetail> getIterationDetails() {
		return iterationDetails;
	}

	public void setIterationDetails(List<IterationDetail> iterationDetails) {
		this.iterationDetails = iterationDetails;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
