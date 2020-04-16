package com.autotest.model;

import java.util.List;

public class TempTestPoint {

	private long planId;
	private int suiteId;
	private List<Long> testPointIds;
	private long outcome;

	public long getPlanId() {
		return planId;
	}

	public void setPlanId(long planId) {
		this.planId = planId;
	}

	public int getSuiteId() {
		return suiteId;
	}

	public void setSuiteId(int suiteId) {
		this.suiteId = suiteId;
	}

	public List<Long> getTestPointIds() {
		return testPointIds;
	}

	public void setTestPointIds(List<Long> testPointIds) {
		this.testPointIds = testPointIds;
	}

	public long getOutcome() {
		return outcome;
	}

	public void setOutcome(long outcome) {
		this.outcome = outcome;
	}

}
