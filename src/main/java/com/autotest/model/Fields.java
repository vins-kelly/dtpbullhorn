package com.autotest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Fields {
	
	@JsonProperty("Microsoft.VSTS.TCM.Steps")
	private String steps;
	
	@JsonProperty("System.State")
	private String state;
	
	@JsonProperty("System.Title")
	private String title;
	
	public Fields() {
		// empty
	}

	public String getSteps() {
		return steps;
	}

	public void setSteps(String steps) {
		this.steps = steps;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Fields [steps=" + steps + ", state=" + state + ", title=" + title + "]";
	}

}
