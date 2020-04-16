package com.autotest.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.autotest.exception.KellyException;

@Service
public class ReferanceXLSXGeneration {

	private static final Logger LOG = LoggerFactory.getLogger(ReferanceXLSXGeneration.class);

	public URI generateXlsx(List<Map<String, Object>> mapList, final String refFileName)
			throws IOException, KellyException { // , final String testCaseId
		try {
			Set<String> keyset = mapList.get(0).keySet();

			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("LogReport");

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 14);
			headerFont.setColor(IndexedColors.GREEN.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			Row headerRow = sheet.createRow(0);

			String[] columns = keyset.stream().toArray(String[]::new);

			for (int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerCellStyle);
			}

			int rowNum = 1;
			int cellNum = 0;

			for (Map<String, Object> map : mapList) {
				Row row = sheet.createRow(rowNum++);
				for (int i = 0; i < columns.length; i++) {
					row.createCell(cellNum++)
							.setCellValue(map.get(columns[i]) != null ? map.get(columns[i]).toString() : null);
				}
				cellNum = 0;
			}

			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}

			final File refFile = new File(refFileName);

			FileOutputStream fileOutputStream = new FileOutputStream(refFile);
			workbook.write(fileOutputStream);
			fileOutputStream.close();
			workbook.close();

			LOG.info("Reference XLSX for " + refFileName + " created Successfully!!!");
			return refFile.toURI();
		} catch (Exception e) {
			throw new KellyException(e.getMessage(), e.getCause());
		}

	}

	public URI generateXlsx1(final Workbook workbook, List<Map<String, Object>> mapList, final String refFileName)
			throws IOException, KellyException { // , final String testCaseId
		try {
			Set<String> keyset = mapList.get(0).keySet();

//			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet(refFileName);

			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 14);
			headerFont.setColor(IndexedColors.GREEN.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);

			Row headerRow = sheet.createRow(0);

			String[] columns = keyset.stream().toArray(String[]::new);

			for (int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerCellStyle);
			}

			int rowNum = 1;
			int cellNum = 0;

			for (Map<String, Object> map : mapList) {
				Row row = sheet.createRow(rowNum++);
				for (int i = 0; i < columns.length; i++) {
					row.createCell(cellNum++)
							.setCellValue(map.get(columns[i]) != null ? map.get(columns[i]).toString() : null);
				}
				cellNum = 0;
			}

			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}

			final File refFile = new File(refFileName);

			FileOutputStream fileOutputStream = new FileOutputStream(refFile);
			workbook.write(fileOutputStream);
			fileOutputStream.close();
//			workbook.close();

			LOG.info("Reference XLSX for " + refFileName + " created Successfully!!!");
			return refFile.toURI();
		} catch (Exception e) {
			throw new KellyException(e.getMessage(), e.getCause());
		}

	}
}
