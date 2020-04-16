package com.autotest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AzureResponse {
	
	@JsonProperty
	private int id;
	
	@JsonProperty
	private int rev;
	
	@JsonProperty("fields")
	private Fields fields;
	
	private List<Steps> stepsList;
	
	public AzureResponse() {
		// empty
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRev() {
		return rev;
	}

	public void setRev(int rev) {
		this.rev = rev;
	}

	public Fields getFields() {
		return fields;
	}

	public void setFields(Fields fields) {
		this.fields = fields;
	}

	public List<Steps> getStepsList() {
		return stepsList;
	}

	public void setStepsList(List<Steps> stepsList) {
		this.stepsList = stepsList;
	}

	@Override
	public String toString() {
		return "RestResponse [id=" + id + ", rev=" + rev + ", fields=" + fields + "]";
	}

}
