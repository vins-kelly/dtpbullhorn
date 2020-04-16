package com.autotest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestSuitNumbers {
	
	@JsonProperty("testSuitNumbers")
	private List<String> testSuitNumbers;

	public List<String> getTestSuitNumbers() {
		return testSuitNumbers;
	}

	public void setTestSuitNumbers(List<String> testSuitNumbers) {
		this.testSuitNumbers = testSuitNumbers;
	}

	@Override
	public String toString() {
		return "TestSuitNumbers [testSuitNumbers=" + testSuitNumbers + "]";
	}
	
	

}
