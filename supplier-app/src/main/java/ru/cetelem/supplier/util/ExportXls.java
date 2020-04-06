package ru.cetelem.supplier.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.ResourceUtils;

import com.vaadin.flow.server.StreamResource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.supplier.ui.view.CarListView;

public class ExportXls {
	private CarListView carListView;

	public ExportXls(CarListView carListView) {
		super();
		this.carListView = carListView;
	}
	
	public StreamResource getCarResource() {
		String fileName = "cars-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")) +
			    LocalTime.now().format(DateTimeFormatter.ofPattern("-HHmmss")) + ".xls";
		return new StreamResource(fileName, () -> createInputStream());
	}

	private InputStream createInputStream() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(exportReport());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JRException e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(outputStream.toByteArray());
	}

    public byte[] exportReport() throws JRException, FileNotFoundException, IOException {  
        File tempFile = File.createTempFile("tmp", ".xls");
        SimpleOutputStreamExporterOutput fileOut = new SimpleOutputStreamExporterOutput(tempFile);        
        List<CarExcel> cars = getCars(carListView.getFilterdCars());
        exportXlsReport(fileOut, cars);    
        byte[] array = Files.readAllBytes(Paths.get(tempFile.getAbsolutePath()));
        return array;
    }

	private List<CarExcel> getCars(List<Car> filterdCars) {
		List<CarExcel> cars = new ArrayList<>();
		for(Car car : filterdCars) {
			CarExcel carExcel = new CarExcel(car);
			cars.add(carExcel);
		}
		return cars;
	}

	private void exportXlsReport(SimpleOutputStreamExporterOutput fileOut, List<CarExcel> cars)
			throws FileNotFoundException, JRException {
		File file = ResourceUtils.getFile("classpath:cars.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(cars);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Cassiope");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        
    	JRXlsExporter xlsExporter = new JRXlsExporter();
        xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        xlsExporter.setExporterOutput(fileOut);
        SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
        SimpleXlsExporterConfiguration xlsExporterConfiguration = new SimpleXlsExporterConfiguration();
        xlsReportConfiguration.setOnePagePerSheet(true);
        xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(false);
        xlsReportConfiguration.setDetectCellType(true);
        xlsReportConfiguration.setWhitePageBackground(false);
        xlsExporter.setConfiguration(xlsReportConfiguration);
        xlsExporter.exportReport();
	}

}
