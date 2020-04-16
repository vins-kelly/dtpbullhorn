package com.autotest.service;

import static com.aventstack.extentreports.Status.ERROR;
import static com.aventstack.extentreports.Status.FAIL;
import static org.apache.poi.common.usermodel.HyperlinkType.FILE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autotest.exception.KellyException;
import com.autotest.model.LogReport;

@Service
public class LogReportService {
	
	private static final Logger LOG = LoggerFactory.getLogger(LogReportService.class);
	private static final String REPORT_PATH1 = "C:\\Kelly Test Reports\\LogReports_" + LocalDate.now() + "\\";
	private static final String REPORT_PATH = "\\\\amer\\dfs\\aimdev\\other\\edw_test_tool\\secure\\Kelly Test Reports\\LogReports_" + LocalDate.now() + "\\";
	private static final String[] COLUMNS = {"Step", "Test Step", "Count", "Outcome"};
	private static final String DATE_FORMAT = "dd-MM-yyyy";
	private static String FILE_STATUS = "Pass_";
	public static String DIRECTORY_NAME = null;
	
	@Autowired
	private ReferanceXLSXGeneration referanceXLSXGeneration;
	
	public String generateXlsx(List<LogReport> logReports, final String testSuitNumber) throws IOException, KellyException { // , final String testCaseId
		try {
			Workbook workbook = new XSSFWorkbook();
			CreationHelper creationHelper = workbook.getCreationHelper();
			Sheet sheet = workbook.createSheet("LogReport");
			
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 14);
			headerFont.setColor(IndexedColors.GREEN.getIndex());
			
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);
			
			Row headerRow = sheet.createRow(0);
			
			for(int i=0; i < COLUMNS.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(COLUMNS[i]);
				cell.setCellStyle(headerCellStyle);
			}
			
			CellStyle dateCellStyle = workbook.createCellStyle();
			dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat(DATE_FORMAT));
			
			createDirectory(testSuitNumber);
			
			int rowNum = 1;
			for(LogReport logReport: logReports) {
				Row row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(logReport.getStepId());
				row.createCell(1).setCellValue(formulateQuery(logReport.getStepQuery()));
				if (logReport.getListRequiredData() != null && !logReport.getListRequiredData().isEmpty()) {
//					row.createCell(2).setHyperlink(createLink(creationHelper, logReport));
//					row.createCell(2).setHyperlink(createLinkNew(creationHelper, logReport.getListRequiredData(), DIRECTORY_NAME + logReport.getTestCaseName() + "_" + logReport.getStepId() + "_" + getCurrentTime() +"_.xlsx"));
//					row.createCell(2).setCellValue("link to data...");
					row.createCell(2).setCellValue(refereceSheet(workbook, logReport.getListRequiredData(), "Step " + logReport.getStepId()));
				} else {
					row.createCell(2).setCellValue(logReport.getRequiredData().toString());	
				}
				
				if (logReport.getStatus().equals(FAIL)) {
					FILE_STATUS = "Fail_";
				} else if(logReport.getStatus().equals(ERROR)) {
					FILE_STATUS = "Error_";
				}
				
				row.createCell(3).setCellValue(logReport.getStatus().toString());
			}
			
			for(int i = 0; i <COLUMNS.length; i++) {
				sheet.autoSizeColumn(i);
			}
			
			FileOutputStream fileOutputStream = new FileOutputStream(DIRECTORY_NAME + FILE_STATUS + logReports.get(0).getTestCaseName() + "_" + getCurrentTime() +"_.xlsx");
			workbook.write(fileOutputStream);
			fileOutputStream.close();
			workbook.close();
			
			LOG.info("XLSX created Successfully!!!");
			return FILE_STATUS = "Pass_";
		} catch (Exception e) {
			throw new KellyException(e.getMessage(), e.getCause());
		}
		
	}
	
	public Hyperlink createLinkNew(final CreationHelper creationHelper, final List<Map<String, Object>> mapList, final String refFileName) throws IOException, KellyException {
		Hyperlink link = creationHelper.createHyperlink(FILE);
		final URI linkString = referanceXLSXGeneration.generateXlsx(mapList, refFileName);
		link.setAddress(linkString.toString());
		
		return link;
	}
	
	public String refereceSheet(final Workbook workbook, final List<Map<String, Object>> mapList, final String refFileName) throws IOException, KellyException {
		final URI linkString = referanceXLSXGeneration.generateXlsx1(workbook, mapList, refFileName);
		return refFileName;
	}
	
	public void createDirectory(final String testSuitNumber) {
		File directoryName = new File(DIRECTORY_NAME != null ? DIRECTORY_NAME : REPORT_PATH + testSuitNumber + "_" + getCurrentTime());
		if(!directoryName.exists()) {
			LOG.info("createDirectory() >> creating log directory!!!");
			directoryName.mkdirs();
			DIRECTORY_NAME = directoryName.toString() + "\\";
		}
	}
	
	private String getCurrentTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH.mm");
		LocalTime currentTime = LocalTime.now();
		
		return currentTime.format(formatter);
	}
	
	private String formulateQuery(final String query) {
		if (query.contains("lt;")) {
			query.replaceAll("&amp;", "");
			return query.replaceAll("&amp;lt;", "<");
		}
		
		if (query.contains("gt;")) {
			query.replaceAll("&amp;", "");
			return query.replaceAll("&amp;gt;", ">");
		}
		
		return query;
	}

}
