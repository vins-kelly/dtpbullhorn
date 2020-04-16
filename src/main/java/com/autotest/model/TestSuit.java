package com.autotest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestSuit {
	
	@JsonProperty("value")
	private List<TestCases> testCases;
	
	@JsonProperty("count")
	private int count;

	public TestSuit() {
		// Hidden
	}

	public List<TestCases> getTestCase() {
		return testCases;
	}

	public void setTestCase(List<TestCases> testCases) {
		this.testCases = testCases;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "TestSuit [testCases=" + testCases + ", count=" + count + "]";
	}

}
