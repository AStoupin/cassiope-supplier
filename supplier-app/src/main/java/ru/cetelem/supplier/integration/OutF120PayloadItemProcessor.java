package ru.cetelem.supplier.integration;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.cetelem.cassiope.supplier.io.f120.F120;
import ru.cetelem.cassiope.supplier.io.f120.F120Item;
import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.cassiope.supplier.model.PayloadItem;
import ru.cetelem.supplier.service.CarService;
import ru.cetelem.supplier.service.PayloadService;
import ru.cetelem.cassiope.supplier.util.DateUtils;


public class OutF120PayloadItemProcessor implements AbstractPayloadItemProcessor , AbstractCarPayloadItemUpdateProcessor {
	private static final Log log = LogFactory.getLog(OutF120PayloadItemProcessor.class);
	
	private CarService carService;
	private Payload payload;
	private PayloadService payloadService;
	
	OutF120PayloadItemProcessor(CarService carService, PayloadService payloadService, Payload payload){
		this.carService = carService;
		this.payload = payload;
		this.payloadService = payloadService;
	}
	
	@Override
	public void  process() {
		process(carService.findByState("READY TO FINANCE"));
	}

	private void createPayloadItems(List<Object> fileRows) {
		for (Object fileItem : fileRows/*Create filerows and return them as a list*/) {
			PayloadItem item =  payloadService.createPayloadItemByObject(payload, fileItem);
			
			if (fileItem instanceof F120Item) {
				F120Item f120Item = (F120Item)fileItem;
				item.eventCode = "F120";
				item.car =  f120Item.car;
				item.car.getPayloadItems().add(item);
				
			}

		}		
		
	}

	@Override
	public void process(List<Car> cars) {
		log.info("OutF120PayloadItemProcessor process list started ");
				
		if(this.payload.payloadItems.size()>0) {
			log.info("payloadItems not empty. clear");
			this.payload.payloadItems.stream().filter(pi->pi.car!=null).forEach(pi->pi.car.getPayloadItems().remove(pi));
			this.payload.payloadItems.clear();
		}
		
		
		F120.F120Builder cb = F120.F120Builder.createF120Builder(DateUtils.asDate(payload.date));

		carService.findByState("READY TO FINANCE").stream().filter(car->cars.contains(car)).forEach(car->
			cb.addF120Item(car.getDealer().getCode(), car.getInvoiceNum(), car.getVin(), 
					Math.round(car.getValue() * 100), car));

		createPayloadItems(cb.build().getAsList());
		
	}


}
