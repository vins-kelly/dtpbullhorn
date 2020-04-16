package com.autotest.service;

import static com.aventstack.extentreports.Status.ERROR;
import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.PASS;
import static com.aventstack.extentreports.Status.WARNING;
import static java.util.Optional.empty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autotest.common.GenericBuilder;
import com.autotest.exception.KellyException;
import com.autotest.model.AzureResponse;
import com.autotest.model.ExtentReport;
import com.autotest.model.LogReport;
import com.autotest.model.Steps;
import com.autotest.repository.AutomationRepository;
import com.aventstack.extentreports.Status;

@Service
public class AutomationService {
	
	private static final Logger LOG = LoggerFactory.getLogger(AutomationService.class);
	private static final String SOURCE_COUNT = "SourceCount";
	private static final String SOURCE_TARGET_COUNT_MATCH = "SourceTargetCountMatch";
	private static final String SOURCE_TARGET_DATA_COMPARE = "Sourcetargetdatacompare";
	private static final String TARGET_SOURCE_DATA_COMPARE = "Targetsourcedatacompare";
	private static final String NULL_CHECK = "NULLcheck";
	private static final String RECONSTEP1_COUNT = "reconstep1_count";
	private static final String RECONSTEP2_COUNT = "reconstep2_count";
	private static final String RECONSTEP3_COUNT = "reconstep3_count";
	private static final String RECONSTEP4_COUNT = "reconstep4_count";
	private static final String DUPLICATE_CHECK = "DuplicateCheck";
	private static final String KSN_SOURCE_DATA = "KSNSourceData";
	private static final String SQL_TARGET_DATA_COMPARE = "SQLTargetDataCompare";

	private int sourceCount;
	private int targetCount;
	private int recon1;
	private int recon2;
	private int recon3;
	private int recon4;
	private List<Map<String, Object>> listSourceData;
	private List<Map<String, Object>> listSqlTargetData;

	@Autowired
	private AutomationRepository automationRepository;

	@Autowired
	private LogReportService logReportService;

	@Autowired
	private ExtentedReports extentedReports;

	private List<LogReport> logReports = new ArrayList<LogReport>();
	private List<ExtentReport> extentReports = new ArrayList<>();

	public ReportDataList iterateSteps(AzureResponse azureResponse, final String testSuitNumber) throws KellyException, IOException {
		azureResponse.getStepsList().stream().forEach(step -> {
			try {
				checkExpectedCondition(step, azureResponse);
			} catch (KellyException e) {
				//
			}
		});

		createLogReport(logReports, testSuitNumber);
		return new ReportDataList(logReports, extentReports);
	}

	public void checkExpectedCondition(final Steps steps, final AzureResponse azureResponse) throws KellyException {
		try {
			String condition = steps.getCondition();

			if (condition.equalsIgnoreCase(SOURCE_COUNT)) {
				sourceCount = automationRepository.count(steps);

				if (sourceCount > 0) {
					extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, PASS)
							.with(ExtentReport::setSteps, steps)
							.with(ExtentReport::setRestResponse, azureResponse).build());
					
					logReports.add(recordLog(steps, sourceCount, null, azureResponse, PASS));
				} else {
					extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, FAIL)
							.with(ExtentReport::setSteps, steps)
							.with(ExtentReport::setRestResponse, azureResponse).build());
					
					logReports.add(recordLog(steps, sourceCount, null, azureResponse, FAIL));
				}

			} else if (condition.equalsIgnoreCase(SOURCE_TARGET_COUNT_MATCH)) {
				targetCount = automationRepository.count(steps);

				if (targetCount == sourceCount) {
					extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, PASS)
							.with(ExtentReport::setSteps, steps)
							.with(ExtentReport::setRestResponse, azureResponse).build());
					
					logReports.add(recordLog(steps, targetCount, null, azureResponse, PASS));
				} else {
					extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, FAIL)
							.with(ExtentReport::setSteps, steps)
							.with(ExtentReport::setRestResponse, azureResponse).build());
					
					logReports.add(recordLog(steps, targetCount, null, azureResponse, FAIL));
				}

			} else if (condition.equalsIgnoreCase(DUPLICATE_CHECK) || condition.equalsIgnoreCase(NULL_CHECK)
					|| condition.equalsIgnoreCase(SOURCE_TARGET_DATA_COMPARE)
					|| condition.equalsIgnoreCase(TARGET_SOURCE_DATA_COMPARE)) { // DUPLICATE_CHECK
				List<Map<String, Object>> list = automationRepository.countList(steps);

				if (list.size() == 0) {
					extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, PASS)
							.with(ExtentReport::setSteps, steps)
							.with(ExtentReport::setRestResponse, azureResponse).build());
					
					logReports.add(recordLog(steps, list.size(), null, azureResponse, PASS));
				} else if (list.size() > 0) {
					extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, FAIL)
							.with(ExtentReport::setSteps, steps)
							.with(ExtentReport::setRestResponse, azureResponse).build());
					
					logReports.add(recordLog(steps, empty(), list, azureResponse, FAIL));
				}

			} else if (condition.equalsIgnoreCase(RECONSTEP1_COUNT)) {
				recon1 = automationRepository.count(steps);

				if (recon1 > 0) {
					extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, PASS)
							.with(ExtentReport::setSteps, steps)
							.with(ExtentReport::setRestResponse, azureResponse).build());
					
					logReports.add(recordLog(steps, recon1, null, azureResponse, PASS));
				} else {
					extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, FAIL)
							.with(ExtentReport::setSteps, steps)
							.with(ExtentReport::setRestResponse, azureResponse).build());
					
					logReports.add(recordLog(steps, recon1, null, azureResponse, FAIL));
				}

			} else if (condition.equalsIgnoreCase(RECONSTEP2_COUNT)) {
				recon2 = automationRepository.count(steps);

				if (recon2 > 0) {
					extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, PASS)
							.with(ExtentReport::setSteps, steps)
							.with(ExtentReport::setRestResponse, azureResponse).build());
					
					logReports.add(recordLog(steps, recon2, null, azureResponse, PASS));
				} else {
					extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, FAIL)
							.with(ExtentReport::setSteps, steps)
							.with(ExtentReport::setRestResponse, azureResponse).build());
					
					logReports.add(recordLog(steps, recon2, null, azureResponse, FAIL));
				}

			} else if (condition.equalsIgnoreCase(RECONSTEP3_COUNT)) {
				recon3 = automationRepository.count(steps);

				if (recon3 == (recon1 - recon2)) {
					extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, PASS)
							.with(ExtentReport::setSteps, steps)
							.with(ExtentReport::setRestResponse, azureResponse).build());
					
					logReports.add(recordLog(steps, recon3, null, azureResponse, PASS));
				} else {
					extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, FAIL)
							.with(ExtentReport::setSteps, steps)
							.with(ExtentReport::setRestResponse, azureResponse).build());
					
					logReports.add(recordLog(steps, recon3, null, azureResponse, FAIL));
				}

			} else if (condition.equalsIgnoreCase(RECONSTEP4_COUNT)) {
				recon4 = automationRepository.count(steps);

				if (recon4 == (recon2 - recon3)) {
					extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, PASS)
							.with(ExtentReport::setSteps, steps)
							.with(ExtentReport::setRestResponse, azureResponse).build());
					
					logReports.add(recordLog(steps, recon4, null, azureResponse, PASS));
				} else {
					extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, FAIL)
							.with(ExtentReport::setSteps, steps)
							.with(ExtentReport::setRestResponse, azureResponse).build());
					
					logReports.add(recordLog(steps, recon4, null, azureResponse, FAIL));
				}

			} else if (condition.equalsIgnoreCase(KSN_SOURCE_DATA)) {
				listSourceData = automationRepository.countList(steps);

				if (listSourceData.size() > 0) {
					extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, PASS)
							.with(ExtentReport::setSteps, steps)
							.with(ExtentReport::setRestResponse, azureResponse).build());
					
					logReports.add(recordLog(steps, listSourceData.size(), null, azureResponse, PASS));
				} else if (listSourceData.size() <= 0) {
					extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, FAIL)
							.with(ExtentReport::setSteps, steps)
							.with(ExtentReport::setRestResponse, azureResponse).build());
					
					logReports.add(recordLog(steps, listSourceData.size(), null, azureResponse, FAIL));
				}

			} else if (condition.equalsIgnoreCase(SQL_TARGET_DATA_COMPARE)) {
				listSqlTargetData = automationRepository.countList(steps);
				
				List<Map<String, Object>> resultList = compareSourceTarget(listSourceData, listSqlTargetData);

				if (resultList.size() == 0) {
					extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, PASS)
							.with(ExtentReport::setSteps, steps)
							.with(ExtentReport::setRestResponse, azureResponse).build());
					
					logReports.add(recordLog(steps, resultList.size(), null, azureResponse, PASS));
				} else if (resultList.size() > 0) {
					extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, FAIL)
							.with(ExtentReport::setSteps, steps)
							.with(ExtentReport::setRestResponse, azureResponse).build());
					
					logReports.add(recordLog(steps, empty(), resultList, azureResponse, FAIL));
				}
				
				listSqlTargetData.clear();
				listSourceData.clear();

			} else {
				LOG.warn("--checkExpectedCondition() Undefined keyword!!!");
				extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, WARNING)
						.with(ExtentReport::setTestCaseName, azureResponse.getId() + "-" + azureResponse.getFields().getTitle())
						.with(ExtentReport::setMessage, " condition keyword is undefined!!!").build());
			}

		} catch (Exception e) {
			extentReports.add(GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, ERROR)
					.with(ExtentReport::setTestCaseName, azureResponse.getId() + "-" + azureResponse.getFields().getTitle())
					.with(ExtentReport::setMessage, "" + e.getMessage()).build());
			
			logReports.add(recordLog(steps, 0, null, azureResponse, ERROR));
			LOG.error(e.getMessage());
		}

	}

	public void createExtentReport(final List<ExtentReport> extentReports, final String testSuitNumber) throws IOException, KellyException {
		if (extentReports.size() > 0) {
			extentedReports.singlrReportMultipleTests(extentReports, testSuitNumber);

			extentReports.clear();
		} else {
			LOG.warn("<< checkLogSize() No steps executed!!!");
		}

	}

	public void createLogReport(final List<LogReport> logReports, final String testSuitNumber) throws IOException, KellyException {
		if (logReports.size() > 0) {
			logReportService.generateXlsx(logReports, testSuitNumber);

			logReports.clear();
		} else {
			LOG.warn("<< checkLogSize() No steps executed!!!");
		}

	}
	
	public List<Map<String, Object>> compareSourceTarget(List<Map<String, Object>> sourceMapList,
			List<Map<String, Object>> targetMapList) {
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < sourceMapList.size(); i++) {
			Map<String, Object> sourceMap = sourceMapList.get(i);

			if (!checkAvailable(sourceMap, targetMapList)) {
				listMap.add(sourceMap);
			}
		}

		return listMap;
	}
	
	public static boolean checkAvailable(Map<String, Object> sourceMap, List<Map<String, Object>> targetMapList) {
		for (int j = 0; j < targetMapList.size(); j++) {
			Map<String, Object> targetMap = targetMapList.get(j);
			if (targetMap.entrySet().containsAll(sourceMap.entrySet())) {
				return true;
			}
		}

		return false;
	}

	private LogReport recordLog(final Steps steps, final Object result, final List<Map<String, Object>> resultList,
			final AzureResponse azureResponse, final Status status) {
		
		return GenericBuilder.of(LogReport::new).with(LogReport::setStepId, steps.getStepId())
			.with(LogReport::setStepQuery, steps.getStepQuery()).with(LogReport::setRequiredData, result)
			.with(LogReport::setStatus, status).with(LogReport::setListRequiredData, resultList)
			.with(LogReport::setTestCaseName, azureResponse.getId() + "-" + azureResponse.getFields().getTitle())
			.build();
	}

}
