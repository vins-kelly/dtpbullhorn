package com.autotest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RunResult {

	@JsonProperty("count")
	private int count;

	@JsonProperty("value")
	private List<ResultValue> resultValue;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<ResultValue> getResultValue() {
		return resultValue;
	}

	public void setResultValue(List<ResultValue> resultValue) {
		this.resultValue = resultValue;
	}

}
