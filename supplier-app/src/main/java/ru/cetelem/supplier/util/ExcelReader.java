package ru.cetelem.supplier.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import ru.cetelem.supplier.service.CarService;

public class ExcelReader {
	private static final Log log = LogFactory.getLog(ExcelReader.class); 

	public static String readFromExcel(InputStream stream, CarService carService)
			throws IOException {
		log.info("ExcelReader readFromExcel started");
		boolean needHeader = true;
		boolean wasHeader = false;

		String vin = null;
		String model = null;
		String dealerCode = null;
		String invoiceNum = null;
		double value = 0;
		double valueFinance = 0;
		String planCode = null;
		Date financeDate = null;
		Date whosaleDate = null;
		String eptsNumber = null;

		String cellValue;
		Date cellDate;
		double cellDouble;

		int cars = 0;

		HSSFWorkbook myExcelBook = new HSSFWorkbook(stream);
		HSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);
		for (int numRow = 0; ; numRow++) {
			if(myExcelSheet.getRow(numRow)==null || myExcelSheet.getRow(numRow).getCell(0)==null || 
					"".equals(myExcelSheet.getRow(numRow).getCell(0).getStringCellValue()))
				break;

			dealerCode = null;
			invoiceNum = null;
			value = 0;
			valueFinance = 0;
			planCode = null;
			financeDate = null;
			whosaleDate = null;
			eptsNumber = null;

			HSSFRow row = myExcelSheet.getRow(numRow);
			if (row == null || row.getCell(0) == null)
				continue;
			for (int numCol = 0; numCol < row.getLastCellNum(); numCol++) {
				Cell cell = row.getCell(numCol);
				if (cell == null)
					continue;
				cellValue = null;
				cellDate = null;
				cellDouble = 0;

				if (cell.getCellType() == CellType.STRING) {
					cellValue = cell.getStringCellValue();
					if (needHeader) {
						if ("VIN".equalsIgnoreCase(cellValue)) {
							wasHeader = true;
							break;
						}
					}
				} else if (cell.getCellType() == CellType.NUMERIC) {
					if (numCol == 6) {
						cellDate = cell.getDateCellValue();
						cellValue = new SimpleDateFormat("dd.MM.yyyy")
								.format(cellDate);
					} else {
						cellDouble = cell.getNumericCellValue();
						cellValue = "" + (long) cellDouble;
						// cellValue = String.valueOf(cellDouble);
					}
				}
/* 
 Excel header:
 Column	Name
	0	vin
	1	dealerCode
	2	invoiceNum
	3	value
	4	valueFinance
	5	planCode
	6	whosaleDate
	7	eptsNumber
 */
				switch (numCol) {
				case 0:
					vin = cellValue;
					break;
				case 1:
					dealerCode = cellValue;
					break;
				case 2:
					invoiceNum = cellValue;
					break;
				case 3:
					value = cellDouble;
					break;
				case 4:
					valueFinance = cellDouble;
					break;
				case 5:
					planCode = cellValue;
					break;
				case 6:
					whosaleDate = cellDate;
					break;
				case 7:
					eptsNumber = cellValue;
					break;
				}
			}
			
			// valueFinance приравниваем value			
			if(valueFinance == 0.0) {
				valueFinance = value;
			}
			if(value == 0.0) {
				value = valueFinance;
			}
			if(valueFinance != value) {
				valueFinance = value;
			}
			
			// dealerCode заполняем нулями слева до длины в 5 символов
			if(dealerCode == null || dealerCode.trim().isEmpty()) {
				dealerCode = "00000";
			} else if(dealerCode.trim().length() < 5){
				dealerCode = "00000".substring(0, 5 - dealerCode.trim().length()) + dealerCode.trim();
			}
			
			if (wasHeader && needHeader) {
				needHeader = false;
			} else if (wasHeader && !needHeader && vin != null
					&& !"".equals(vin)) {
				if (carService.newCar("NEW", vin, model, dealerCode,
						invoiceNum, value, valueFinance, planCode, financeDate,
						whosaleDate, eptsNumber) != null) {
					cars++;
				}
			}
		}
		myExcelBook.close();
		return cars + " car(s) was added. ";
	}

}
