package com.autotest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.autotest.model.ExtentReport;
import com.autotest.model.LogReport;

@Service
public class ReportDataList {
	
	private List<LogReport> logReports;
	private List<ExtentReport> extentReports;
	
	public ReportDataList() {
		// Hidden
	}
	
	public ReportDataList(List<LogReport> logReports, List<ExtentReport> extentReports) {
		this.logReports = logReports;
		this.extentReports = extentReports;
	}

	public List<LogReport> getLogReports() {
		return logReports;
	}

	public void setLogReports(List<LogReport> logReports) {
		this.logReports = logReports;
	}

	public List<ExtentReport> getExtentReports() {
		return extentReports;
	}

	public void setExtentReports(List<ExtentReport> extentReports) {
		this.extentReports = extentReports;
	}

	@Override
	public String toString() {
		return "ReportDataList [logReports=" + logReports + ", extentReports=" + extentReports + "]";
	}

}
