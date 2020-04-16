package com.autotest.model;

import java.util.List;
import java.util.Map;

import com.aventstack.extentreports.Status;

public class LogReport {
	
	private long stepId;
	private String stepQuery;
	private Object requiredData;
	private List<Map<String, Object>> listRequiredData;
	private Status status;
	private String testCaseName;
	
	public LogReport() {
		// Hidden
	}

	public long getStepId() {
		return stepId;
	}

	public void setStepId(long stepId) {
		this.stepId = stepId;
	}

	public String getStepQuery() {
		return stepQuery;
	}

	public void setStepQuery(String stepQuery) {
		this.stepQuery = stepQuery;
	}

	public Object getRequiredData() {
		return requiredData;
	}

	public void setRequiredData(Object requiredData) {
		this.requiredData = requiredData;
	}

	public List<Map<String, Object>> getListRequiredData() {
		return listRequiredData;
	}

	public void setListRequiredData(List<Map<String, Object>> listRequiredData) {
		this.listRequiredData = listRequiredData;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}

	@Override
	public String toString() {
		return "LogReport [stepId=" + stepId + ", stepQuery=" + stepQuery + ", requiredData=" + requiredData + "]";
	}

}
