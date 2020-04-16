package com.autotest.model;

import static org.apache.commons.lang3.StringUtils.substringBetween;

import java.util.Arrays;
import java.util.List;

import com.autotest.exception.KellyException;

public class Steps {
	
	private long stepId;
	private String stepQuery;
	private String expectedResult;
	private String condition;
	
	public Steps() {
		// Hidden
	}
	
	public Steps(long stepId, String stepQuery, String expectedResult) {
		this.stepId = stepId;
		this.stepQuery = stepQuery;
		this.expectedResult = expectedResult;
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
	
	public String getExpectedResult() throws KellyException {
		return seperateValues().get(1);
	}
	
	public void setExpectedResult(String expectedResult) {
		this.expectedResult = expectedResult;
	}
	
	public String getCondition() throws KellyException {
		return seperateValues().get(0);
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		return "Steps [stepId=" + stepId + ", stepQuery=" + stepQuery + ", expectedResult=" + expectedResult + "]";
	}
	
	private List<String> seperateValues() throws KellyException {
		try {
			String value = substringBetween(expectedResult, "[", "]");
			return Arrays.asList(value.split(","));
		} catch (Exception e) {
			throw new KellyException(e.getMessage(), e.getCause());
		}
		
	}

}
