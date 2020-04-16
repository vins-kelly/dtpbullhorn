package com.autotest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestCases {
	
	@JsonProperty("testCase")
	private TestCase testCase;

	public TestCases() {
		//
	}

	public TestCase getTestCase() {
		return testCase;
	}

	public void setTestCase(TestCase testCase) {
		this.testCase = testCase;
	}

	@Override
	public String toString() {
		return "TestCases [testCase=" + testCase + "]";
	}

}
