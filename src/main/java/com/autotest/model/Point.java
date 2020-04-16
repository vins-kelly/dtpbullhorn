package com.autotest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Point {
	
	@JsonProperty("id")
	private long id;
	
	@JsonProperty("url")
	private String url;
	
	@JsonProperty("assignedTo")
	private AssignedTo assignedTo;
	
	@JsonProperty("automated")
	private boolean automated;
	
	@JsonProperty("lastTestRun")
	private LastTestRun lastTestRun;
	
	@JsonProperty("lastResult")
	private LastResult lastResult;
	
	@JsonProperty("outcome")
	private String outcome;
	
	@JsonProperty("state")
	private String state;
	
	@JsonProperty("lastResultState")
	private String lastResultState;
	
	@JsonProperty("testCase")
	private TestCaseForPoint testCase;

}
