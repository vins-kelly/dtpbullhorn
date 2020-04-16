package com.autotest.configuration;

import static com.autotest.common.Common.URL_FEATCH_EXCEPTION;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestTemplate;

import com.autotest.exception.KellyException;
import com.autotest.model.Attachment;
import com.autotest.model.AzureResponse;
import com.autotest.model.RunResult;
import com.autotest.model.TempTestPoint;
import com.autotest.model.TestCases;
import com.autotest.model.TestSuit;
import com.autotest.model.WriteResult;

@Configuration
@EnableRetry
public class ConsumeWebServices {
	
	private static final Logger LOG = getLogger(ConsumeWebServices.class);
	private static final String TEST_SUIT_URL1 = "https://dev.azure.com/kellyservices/AIM/_apis/test/Plans/10946/suites/";
	private static final String TEST_SUIT_URL = "https://dev.azure.com/kellyservices/DTPBullhorn%20Reporting/_apis/test/Plans/10946/suites";
	private static final int MAX_RETRIES = 5;
	private static final String REPORT_PATH = "\\\\amer\\dfs\\aimdev\\other\\edw_test_tool\\secure\\Kelly Test Reports\\LogReports_2020-04-07\\13699_19.02\\Fail_14617-Dimjobcodemiddleoffice_19.02_.xlsx";
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private YAMLConfig yamlConfig;
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	public AzureResponse getFieldsDetails(TestCases testCases) throws KellyException {
		LOG.info("getFieldsDetails() >> getting data from Azure...");

		try {
			final byte[] authBytes = yamlConfig.getPat().getBytes(UTF_8);

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(APPLICATION_JSON));
			headers.setBasicAuth(Base64.getEncoder().encodeToString(authBytes));
			HttpEntity<AzureResponse> entity = new HttpEntity<AzureResponse>(headers);

			return restTemplate.exchange(testCases.getTestCase().getTestCaseUrl(), GET, entity, AzureResponse.class).getBody();
		} catch (Exception e) {
			throw new KellyException(URL_FEATCH_EXCEPTION);
		}

	}
	
	@Retryable(value = {KellyException.class, NullPointerException.class}, maxAttempts = MAX_RETRIES)
	public TestSuit getTestCaseId(final String testSuitId) throws KellyException {
		LOG.info("getFieldsDetails() >> getting data from Azure...");

		try {
			final byte[] authBytes = yamlConfig.getPat().getBytes(UTF_8);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(APPLICATION_JSON));
			headers.setBasicAuth(Base64.getEncoder().encodeToString(authBytes));
			HttpEntity<TestSuit> entity = new HttpEntity<TestSuit>(headers);

//			return restTemplate.exchange(TEST_SUIT_URL.concat(testSuitId + "/testcases?api-version=5.0"), GET, entity, TestSuit.class).getBody();
			return restTemplate.exchange("https://dev.azure.com/kellyservices/DTPBullhorn Reporting/_apis/test/Plans/10946/suites/13699/testcases?api-version=5.0", GET, entity, TestSuit.class).getBody();
		} catch (Exception e) {
			LOG.error("--getTestCaseId1() ", e.getMessage());
			throw new KellyException(URL_FEATCH_EXCEPTION, e.getCause());
		}

	}
	
	@Retryable(value = {KellyException.class, NullPointerException.class}, maxAttempts = MAX_RETRIES)
	public String getTestCaseIdString(final String testSuitId) throws KellyException {
		LOG.info("getFieldsDetails() >> getting data from Azure...");

		try {
			final byte[] authBytes = yamlConfig.getPat().getBytes(UTF_8);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(APPLICATION_JSON));
			headers.setBasicAuth(Base64.getEncoder().encodeToString(authBytes));
			HttpEntity<String> entity = new HttpEntity<String>(headers);

//			return restTemplate.exchange(TEST_SUIT_URL.concat(testSuitId + "/testcases?api-version=5.0"), GET, entity, TestSuit.class).getBody();
			return restTemplate.exchange("https://dev.azure.com/kellyservices/DTPBullhorn Reporting/_apis/test/Plans/10946/suites/13699/testcases?api-version=5.0", GET, entity, String.class).getBody();
		} catch (Exception e) {
			LOG.error("--getTestCaseId1() ", e.getMessage());
			throw new KellyException(URL_FEATCH_EXCEPTION, e.getCause());
		}

	}
	
	@Retryable(value = {KellyException.class, NullPointerException.class}, maxAttempts = MAX_RETRIES)
	public RunResult writeStatus() throws KellyException {
		LOG.info("writeStatus() >> posting status to test case to Azure...");

		try {
			final byte[] authBytes = yamlConfig.getPat().getBytes(UTF_8);
			
			WriteResult writeResult = new WriteResult();
			writeResult.setId(100000);
			writeResult.setState("Completed");
			writeResult.setOutcome("Failed");
			
			List<WriteResult> writeResults = new ArrayList<WriteResult>();
			writeResults.add(writeResult);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(APPLICATION_JSON));
			headers.setBasicAuth(Base64.getEncoder().encodeToString(authBytes));
			HttpEntity<List<WriteResult>> entity = new HttpEntity<List<WriteResult>>(writeResults, headers);
			
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			restTemplate = new RestTemplate(requestFactory);
			return restTemplate.exchange("https://dev.azure.com/kellyservices/AIM/_apis/test/Runs/1004204/results?api-version=5.0-preview.5", PATCH, entity, RunResult.class).getBody();
		} catch (Exception e) {
			LOG.error("--writeStatus() ", e.getMessage());
			throw new KellyException(URL_FEATCH_EXCEPTION, e.getCause());
		}

	}
	
	@Retryable(value = {KellyException.class, NullPointerException.class}, maxAttempts = MAX_RETRIES)
	public String writeStatusUsingTestPoint() throws KellyException {
		LOG.info("writeStatus() >> posting status to test case to Azure...");

		try {
			final byte[] authBytes = yamlConfig.getPat().getBytes(UTF_8);

			List<Long> testPointIds = new ArrayList<Long>();
			testPointIds.add(3392L);
			
			TempTestPoint tempTestPoint = new TempTestPoint();
			tempTestPoint.setPlanId(10946);
			tempTestPoint.setSuiteId(14211);
			tempTestPoint.setTestPointIds(testPointIds);
			tempTestPoint.setOutcome(3);
			
			List<TempTestPoint> testPoints = new ArrayList<TempTestPoint>();
			testPoints.add(tempTestPoint);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(APPLICATION_JSON));
			headers.setBasicAuth(Base64.getEncoder().encodeToString(authBytes));
			HttpEntity<TempTestPoint> entity = new HttpEntity<TempTestPoint>(tempTestPoint, headers);
			
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			restTemplate = new RestTemplate(requestFactory);
			return restTemplate.exchange("https://dev.azure.com/kellyservices/AIM/_api/_testManagement/BulkMarkTestPoints", HttpMethod.POST, entity, String.class).getBody();
		} catch (Exception e) {
			LOG.error("--writeStatus() ", e.getMessage());
			throw new KellyException(URL_FEATCH_EXCEPTION, e.getCause());
		}

	}
	
	@Retryable(value = {KellyException.class, NullPointerException.class}, maxAttempts = MAX_RETRIES)
	public String writeAttachment() throws KellyException {
		LOG.info("writeAttachment() >> posting status to test case to Azure...");

		try {
			final byte[] authBytes = yamlConfig.getPat().getBytes(UTF_8);

			Attachment attachment = new Attachment();
			attachment.setStream(base64Convertor(REPORT_PATH));
			attachment.setFileName("Fail_14617-Dimjobcodemiddleoffice_18.08_.xlsx");
			attachment.setComment("Test attachment upload by java code");
			attachment.setAttachmentType("GeneralAttachment");
			
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(APPLICATION_JSON));
			headers.setBasicAuth(Base64.getEncoder().encodeToString(authBytes));
			HttpEntity<Attachment> entity = new HttpEntity<Attachment>(attachment, headers);
			
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			restTemplate = new RestTemplate(requestFactory);
			return restTemplate.exchange("https://dev.azure.com/kellyservices/DTPBullhorn Reporting/_apis/test/Runs/1004266/Results/100000/attachments?api-version=5.1-preview.1", HttpMethod.POST, entity, String.class).getBody();
		} catch (Exception e) {
			LOG.error("--writeStatus() ", e.getMessage());
			throw new KellyException(URL_FEATCH_EXCEPTION, e.getCause());
		}

	}

	
	// method to fetch test suit data using url for Future use
	@Retryable(value = { KellyException.class, NullPointerException.class }, maxAttempts = MAX_RETRIES)
	public TestSuit getTestCaseIdByUrl(final String testSuitUrl) throws KellyException {
		LOG.info("getFieldsDetails() >> getting data from Azure...");

		try {
			final byte[] authBytes = yamlConfig.getPat().getBytes(UTF_8);

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(APPLICATION_JSON));
			headers.setBasicAuth(Base64.getEncoder().encodeToString(authBytes));
			HttpEntity<TestSuit> entity = new HttpEntity<TestSuit>(headers);

			return restTemplate.exchange(testSuitUrl, GET, entity, TestSuit.class).getBody();
		} catch (Exception e) {
			LOG.error("--getTestCaseId1() ", e.getMessage());
			throw new KellyException(URL_FEATCH_EXCEPTION, e.getCause());
		}

	}
	
	private static String base64Convertor(String filePath) throws IOException {
		byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
		return Base64.getEncoder().encodeToString(fileContent);
	}


}
