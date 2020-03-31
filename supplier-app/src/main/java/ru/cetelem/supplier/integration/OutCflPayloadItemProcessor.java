package ru.cetelem.supplier.integration;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import ru.cetelem.cassiope.supplier.io.cfl.Cfl;
import ru.cetelem.cassiope.supplier.io.cfl.Cfl11Item;
import ru.cetelem.cassiope.supplier.io.cfl.Cfl22Item;
import ru.cetelem.cassiope.supplier.io.f150.F150;
import ru.cetelem.cassiope.supplier.io.f150.F150Item;
import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.cassiope.supplier.model.PayloadItem;
import ru.cetelem.supplier.service.CarService;
import ru.cetelem.supplier.service.PayloadService;
import ru.cetelem.supplier.util.Configurator;
import ru.cetelem.cassiope.supplier.util.DateUtils;


public class OutCflPayloadItemProcessor implements AbstractPayloadItemProcessor , AbstractCarPayloadItemUpdateProcessor {
	private static final Log log = LogFactory.getLog(OutCflPayloadItemProcessor.class);
	
	private CarService carService;
	private Payload payload;
	private PayloadService payloadService;
	private Environment environment;
	

	OutCflPayloadItemProcessor(CarService carService, PayloadService payloadService, Payload payload, 
			Environment environment){
		
		this.carService = carService;
		this.payload = payload;
		this.payloadService = payloadService;
		this.environment = environment;
	}
	
	@Override
	public void  process() {
		List<Car> cars = carService.findByState("CANCEL");
		cars.addAll(carService.findByState("READY TO SUBMIT"));
		process(cars);
	}
	

		

	@Override
	public void process(List<Car> cars) {
		log.info("OutCflPayloadItemProcessor process started");

		if(this.payload.payloadItems.size()>0) {
			log.info("payloadItems not empty. clear");
			this.payload.payloadItems.stream().filter(pi->pi.car!=null).forEach(pi->pi.car.getPayloadItems().remove(pi));
			this.payload.payloadItems.clear();
		}	
		
		Cfl.CflBuilder cb = Cfl.CflBuilder
				.createCflBuilder(DateUtils.asDate(payload.date), payload.sequenceNumber, 
						environment.getProperty(Configurator.CREATOR_COMPANY) /* "UR" or "HL" */ );
		

		carService.findByState("CANCEL").stream().filter(car->cars.contains(car))
			.forEach(car->
			cb.addCfl11Item(car.getDealer() == null ? "" : car.getDealer().getCode(), 
					car.getInvoiceNum(), car.getVin(),
					new Date(), new Date(), "RUR", 
					Math.round(car.getValue() * 100), car));

		carService.findByState("READY TO SUBMIT").stream().filter(car->cars.contains(car))
			.forEach(car->
				cb.addCfl22Item(car.getDealer().getCode(), car.getInvoiceNum(), car.getVin(), 
						Math.round(car.getValue() * 100), 
						Math.round(car.getValueFinance() * 100),
						Math.round(car.getValue() * 18), 						// НДС. VAT - Value Added Tax
						"", "", "", "RUR", 
//						car.getIssueDate(), 
//						car.getSubmitDate(), 
						convertLocalDateToDate(car.getFinanceDate()), 			// Проверить
						convertLocalDateToDate(car.getFullRepaymentDate()), 	// Проверить
						car.getCarModel().getCode(), 
						car.getFinancePlan().getCode(), car));

		createPayloadItems(cb.build().getAsList());
		
		log.info("OutCflPayloadItemProcessor process finished");		
	}	
	
	public Date convertLocalDateToDate(LocalDate localDate) {
		if (localDate == null) {
			return null;
		} else {
			return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//			return java.sql.Date.valueOf(localDate);
//			return new SimpleDateFormat("yyyy-MM-dd").parse(localDate.toString());
		}
	}

	private void createPayloadItems(List<Object> fileRows) {
		log.info("createPayloadItems started");
		for (Object fileItem : fileRows) {
			if (fileItem != null) {
				PayloadItem item =  payloadService.createPayloadItemByObject(payload, fileItem);
				
				if (fileItem instanceof Cfl22Item) {
					Cfl22Item cfl22Item = (Cfl22Item)fileItem;
					item.eventCode = "22";
					item.car =  cfl22Item.car;
					if (item.car != null) {
						item.car.getPayloadItems().add(item);
					}
				} else if (fileItem instanceof Cfl11Item) {
					Cfl11Item cfl11Item = (Cfl11Item)fileItem;
					item.eventCode = "11";
					item.car =  cfl11Item.car;
					if (item.car != null) {
						item.car.getPayloadItems().add(item);
					}
				}
			}
		}
		log.info("createPayloadItems finished");
	}

}
