package com.autotest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestCase {
	
	@JsonProperty("id")
	private String testCaseId;
	
	@JsonProperty("url")
	private String testCaseUrl;
	
	@JsonProperty("webUrl")
	private String testCaseWebUrl;

	public TestCase() {
		// Hidden
	}

	public String getTestCaseId() {
		return testCaseId;
	}

	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}

	public String getTestCaseUrl() {
		return testCaseUrl;
	}

	public void setTestCaseUrl(String testCaseUrl) {
		this.testCaseUrl = testCaseUrl;
	}

	public String getTestCaseWebUrl() {
		return testCaseWebUrl;
	}

	public void setTestCaseWebUrl(String testCaseWebUrl) {
		this.testCaseWebUrl = testCaseWebUrl;
	}

	@Override
	public String toString() {
		return "TestCase [testCaseId=" + testCaseId + ", testCaseUrl=" + testCaseUrl + ", testCaseWebUrl="
				+ testCaseWebUrl + "]";
	}

}
