package com.autotest.service;

import static com.aventstack.extentreports.Status.FAIL;
import static com.aventstack.extentreports.Status.PASS;

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
public class ConditionService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ConditionService.class);
	private static final String SOURCE_COUNT = "SourceCount";
	private static final String SOURCE_TARGET_COUNT_MATCH = "SourceTargetCountMatch";
	
	@Autowired
	private AutomationRepository automationRepository;
	
	private List<LogReport> logReports = new ArrayList<LogReport>();
	private List<ExtentReport> extentReports = new ArrayList<>();
	
	public int sourceCount(final Steps steps, final AzureResponse azureResponse) throws KellyException {
		int sourceCount = automationRepository.count(steps);

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
		
		return sourceCount;
	}
	
	public synchronized ExtentReport sourceTargetCountAndCompare(final Steps steps, final AzureResponse azureResponse) throws KellyException{
		String condition = steps.getCondition();
		int sourceCount;
//		int sourceCountToCompare = sourceCount;
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
			int targetCount = automationRepository.count(steps);
			
			if (targetCount == targetCount) {
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

		}
		
		return GenericBuilder.of(ExtentReport::new).with(ExtentReport::setStatus, PASS)
				.with(ExtentReport::setSteps, steps)
				.with(ExtentReport::setRestResponse, azureResponse).build();
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
