package com.autotest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultValue {

	@JsonProperty("id")
	private long id;
	
	@JsonProperty("project")
	private Project project;
	
	@JsonProperty("outcome")
	private String outcome;
	
	@JsonProperty("lastUpdatedDate")
	private String lastUpdatedDate;
	
	@JsonProperty("priority")
	private long priority;
	
	@JsonProperty("url")
	private String url;
	
	@JsonProperty("lastUpdatedBy")
	private LastUpdatedBy lastUpdatedBy;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public LastUpdatedBy getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(LastUpdatedBy lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

}
