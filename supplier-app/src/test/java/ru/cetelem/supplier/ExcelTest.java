package ru.cetelem.supplier;

import static org.junit.Assert.*;

import org.junit.Test;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

public class ExcelTest {

	@Test
	public void test() {
		String current = null;
		try {
			current = new java.io.File(".").getCanonicalPath();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("Current dir:" + current);
		String currentDir = System.getProperty("user.dir");
		System.out.println("Current dir using System:" + currentDir);

		String[] args = { "-f", "./input/123.txt" };
		try {
			POIExample.mainRun(args);
			assertNotNull(args);
		} catch (Exception e) {
			fail("Exception in POIExample");
			e.printStackTrace();
		}
		
		try {
			readFromExcel("./output/example.xls");
		} catch (IOException e) {
			fail("Exception in readFromExcel");
			e.printStackTrace();
		}
	}
	
	public static void readFromExcel(String file) throws IOException{
        HSSFWorkbook myExcelBook = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet myExcelSheet = myExcelBook.getSheet("Отчет за V квартал");
        HSSFRow row = myExcelSheet.getRow(0);
        
        if(row.getCell(0).getCellType() == CellType.STRING){
            String name = row.getCell(0).getStringCellValue();
            System.out.println("Title : " + name);
        }
        
        row = myExcelSheet.getRow(1);
        
        if(row.getCell(0).getCellType() == CellType.STRING){
            String num = row.getCell(0).getStringCellValue();
            System.out.println("Data : " + num);
        }
        
//        if(row.getCell(1).getCellType() == CellType.NUMERIC){
//            Date birthdate = row.getCell(1).getDateCellValue();
//            System.out.println("birthdate :" + birthdate);
//        }
        
        myExcelBook.close();
        
    }

}
