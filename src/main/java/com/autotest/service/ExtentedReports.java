package com.autotest.service;

import static com.aventstack.extentreports.Status.ERROR;
import static com.aventstack.extentreports.Status.FATAL;
import static com.aventstack.extentreports.Status.SKIP;
import static com.aventstack.extentreports.Status.WARNING;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.autotest.exception.KellyException;
import com.autotest.model.ExtentReport;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

@Service
public class ExtentedReports {
	
	private static final Logger LOG = LoggerFactory.getLogger(ExtentReports.class);
	private static final String EXTENT_REPORT_PATH = "C:\\Kelly Test Reports\\ExtentReports_" + LocalDate.now() + "\\";
	
	public void singlrReportMultipleTests(List<ExtentReport> extentReport, final String testSuitNumber) throws IOException, KellyException {
		ExtentReports extentReports = new ExtentReports();
		createDirectory();
		String testCaseName = null;
		ExtentTest test = null;
		
		final String extentReportName = EXTENT_REPORT_PATH.concat("ExtentReport_" + testSuitNumber + "_" + getCurrentTime() + ".html");
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(extentReportName);
	    
        extentReports.attachReporter(htmlReporter);

        ListIterator<ExtentReport> exListIterator = extentReport.listIterator();

		while (exListIterator.hasNext()) {
			ExtentReport extentReport2 = (ExtentReport) exListIterator.next();
			
			try {
				if (extentReport2.getMessage() != null) {
					if (!extentReport2.getTestCaseName().equalsIgnoreCase(testCaseName)) {
						testCaseName = extentReport2.getTestCaseName();
						test = extentReports.createTest(testCaseName);
					}
				} else {
					if (!(extentReport2.getAzureResponse().getId() + "-" + extentReport2.getAzureResponse().getFields().getTitle()).equalsIgnoreCase(testCaseName)) {
						testCaseName = extentReport2.getAzureResponse().getId() + "-" + extentReport2.getAzureResponse().getFields().getTitle();
						test = extentReports.createTest(testCaseName);
					}
				}
			} catch (Exception e) {
				LOG.error(e.getMessage());
				throw new KellyException(e.getMessage(), e.getCause());
			}
			
			if (extentReport2.getStatus().equals(WARNING) || extentReport2.getStatus().equals(ERROR) 
					|| extentReport2.getStatus().equals(SKIP) || extentReport2.getStatus().equals(FATAL)) {
				test.log(extentReport2.getStatus(), extentReport2.getMessage());
			} else {
				test.log(extentReport2.getStatus(), extentReport2.getSteps().getExpectedResult());
			}
		}
		
		extentReports.flush();
	}
	
	public void createDirectory() {
		File directoryName = new File(EXTENT_REPORT_PATH);
		if(!directoryName.exists()) {
			LOG.info("--createDirectory() creating extent directory!!!");
			directoryName.mkdirs();
		}
	}
	
	private static String getCurrentTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH.mm");
		LocalTime currentTime = LocalTime.now();
		
		return currentTime.format(formatter);
	}

}
