package com.autotest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ContextPoint {
	
	@JsonProperty("value")
	private List<Point> points;
	
	@JsonProperty("count")
	private long count;

}
