package com.autotest.service;

import static com.autotest.service.LogReportService.DIRECTORY_NAME;
import static com.aventstack.extentreports.Status.ERROR;
import static com.aventstack.extentreports.Status.FATAL;
import static com.aventstack.extentreports.Status.SKIP;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autotest.common.GenericBuilder;
import com.autotest.configuration.ConsumeWebServices;
import com.autotest.exception.KellyException;
import com.autotest.model.AzureResponse;
import com.autotest.model.ExtentReport;
import com.autotest.model.RunResult;
import com.autotest.model.Steps;
import com.autotest.model.TestCases;
import com.autotest.model.TestSuit;
import com.autotest.model.TestSuitNumbers;

@Service
public class TestExecutorService {
	
	private static final Logger LOG = getLogger(TestExecutorService.class);
	private static final Pattern TAG_REGEX = Pattern.compile("<parameterizedString isformatted=\"true\">(.+?)</parameterizedString>", Pattern.DOTALL);
	private static final String TEST_CASE_STATE = "Ready";
	private static final String NOT_SUITABLE = "Test Case not suitable for Automation.Please follow prerequisit guidlines!!";
	private static final String NO_PERMISSION = "Test Case is not in Ready state!!!";
	private static final String ERROR_REPORT = "Error Report";
	
	@Autowired
	private AutomationService automationService;
	
	@Autowired
	private ConsumeWebServices consumeWebServices;
	
	@Autowired
	private static ReportDataList reportDataList;
	
	private List<ExtentReport> extentReports = new ArrayList<ExtentReport>();
	AzureResponse azureResponse = null;
	
	public String testExecute(TestSuitNumbers testSuitNumbers) throws IOException, KellyException {
		ListIterator<String> testSuitIDListIterator = testSuitNumbers.getTestSuitNumbers().listIterator();

		while (testSuitIDListIterator.hasNext()) {
			String testSuitNumber = testSuitIDListIterator.next();

			try {
//				RunResult runResult = consumeWebServices.writeStatus();
//				String writeByPoints = consumeWebServices.writeStatusUsingTestPoint();
				String writeAttachment = consumeWebServices.writeAttachment();
//				String test = consumeWebServices.getTestCaseIdString(testSuitNumber);
				TestSuit testSuits = consumeWebServices.getTestCaseId(testSuitNumber);

				List<TestCases> testCasesList = testSuits.getTestCase();

				ListIterator<TestCases> testCasesListIterator = testCasesList.listIterator();

				while (testCasesListIterator.hasNext()) {
					TestCases testCases = testCasesListIterator.next();

					azureResponse = consumeWebServices.getFieldsDetails(testCases);

					boolean permission = checkForReadyState(azureResponse);

					if (permission) {
						String result = Arrays.toString(getTagValues(azureResponse.getFields().getSteps()).toArray());

						try {
							List<Steps> stepsList = getSteps(stringFormatter(result));
//							List<Steps> stepsList = getStepsFromSquareBrackets(stringFormatter(result));

							azureResponse.setStepsList(stepsList);

							reportDataList = automationService.iterateSteps(azureResponse, testSuitNumber);
						} catch (Exception e) {
							extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, FATAL)
									.with(ExtentReport::setTestCaseName,
											azureResponse.getId() + "-" + azureResponse.getFields().getTitle())
									.with(ExtentReport::setMessage, "" + e.getMessage()).build());
						}
					} else {
						extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, SKIP)
								.with(ExtentReport::setTestCaseName,
										azureResponse.getId() + "-" + azureResponse.getFields().getTitle())
								.with(ExtentReport::setMessage, NO_PERMISSION).build());
					}

				}
				DIRECTORY_NAME = null;
			} catch (Exception e) {
				LOG.error("--run() failed to execute!!!", e.getCause());
				extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, ERROR)
						.with(ExtentReport::setTestCaseName, ERROR_REPORT)
						.with(ExtentReport::setMessage, "" + e.getMessage()).build());
			} finally {
				if (reportDataList != null) {
					extentReports.addAll(reportDataList.getExtentReports());
				}

				automationService.createExtentReport(extentReports, testSuitNumber);
				
				if (reportDataList != null || !extentReports.isEmpty()) {
					reportDataList.getExtentReports().clear();
					extentReports.clear();
				}
			}
		}
		
		return "Success";
	}
	
	private static List<String> getTagValues(final String str) {
	    final List<String> tagValues = new ArrayList<String>();
	    final Matcher matcher = TAG_REGEX.matcher(str);
	    while (matcher.find()) {
	        tagValues.add(matcher.group(1));
	    }
	    
	    return tagValues;
	}
	
	private static String stringFormatter(final String response) {
		return response.replaceAll("&lt;DIV&gt;", "")
				.replaceAll("&lt;BR/&gt;", "")
				.replaceAll("&lt;/P&gt;", "")
				.replaceAll("&lt;/DIV&gt;", "")
				.replaceAll("&lt;P&gt;", "");
	}
	
	private List<Steps> getSteps(final String stringToMap) throws KellyException {
		try {
			String[] pairs = splitString(stringToMap);
			List<Steps> stepsList = new ArrayList<Steps>();
			int count = 1;

			for (int i = 0; i < pairs.length; i++) {
				String pair = pairs[i].replace("[", "").trim();
				String pair1 = pairs[i + 1].trim();

				if (isStepValid(pair, pair1)) {
					stepsList.add(new Steps(count, pair, pair1));
				} else {
					LOG.error("Step ID : " + count + " is not not valid for Automation!!!");
					throw new KellyException(NOT_SUITABLE + "Step ID : " + count + " is not not valid for Automation!!!");
				}

				count++;
				i++;
			}

			return stepsList;
		} catch (Exception e) {
			LOG.error("--getSteps()", e.getMessage());
			throw new KellyException(e.getMessage(), e.getCause());
		}

	}
	
	private List<Steps> getStepsFromSquareBrackets(final String stringToMap) throws KellyException {
		try {
			String[] pairs = splitString(stringToMap);
			List<Steps> stepsList = new ArrayList<Steps>();
			int count = 1;

			for (int i = 0; i < pairs.length; i++) {
				String pair = StringUtils.substringBetween(pairs[i], "[", "]").replace("[", "").trim();
				String pair1 = pairs[i + 1].trim();

				if (isStepValid(pair, pair1)) {
					stepsList.add(new Steps(count, pair, pair1));
				} else {
					LOG.error("Step ID : " + count + " is not not valid for Automation!!!");
					throw new KellyException("Step ID : " + count + " is not not valid for Automation!!!");
				}

				count++;
				i++;
			}

			return stepsList;
		} catch (Exception e) {
			LOG.error("--getSteps()", e.getMessage());
			throw new KellyException(e.getMessage(), e.getCause());
		}

	}
	
	private boolean checkForReadyState(AzureResponse azureResponse) {
		if (azureResponse.getFields().getState().equalsIgnoreCase(TEST_CASE_STATE)) {
			return true;
		}
		
		return false;
	}
	
	private static String[] splitString(String stringToMap) throws KellyException {
		stringToMap = stringToMap.replaceAll("\n", "");
		return stringToMap.split("\\|,");
	}
	
	private static boolean isStepValid(final String pair, final String pair1) throws KellyException {
		if ((pair.toUpperCase().startsWith("SELECT") || pair.toUpperCase().startsWith("(SELECT")) && (pair1.startsWith("[")) && pair1.endsWith("]")) {
			return true;
		}

		return false;
	}

}
