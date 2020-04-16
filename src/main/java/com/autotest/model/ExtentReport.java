package com.autotest.model;

import com.aventstack.extentreports.Status;

public class ExtentReport {
	
	private Status status;
	private Steps steps;
	private AzureResponse azureResponse;
	private String message;
	private String testCaseName;
	
	public ExtentReport() {
		// Hidden
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Steps getSteps() {
		return steps;
	}
	
	public void setSteps(Steps steps) {
		this.steps = steps;
	}

	public AzureResponse getAzureResponse() {
		return azureResponse;
	}

	public void setRestResponse(AzureResponse azureResponse) {
		this.azureResponse = azureResponse;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	@Override
	public String toString() {
		return "ExtentReport [status=" + status + ", steps=" + steps + ", azureResponse=" + azureResponse + "]";
	}

}
