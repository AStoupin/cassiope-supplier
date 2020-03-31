package ru.cetelem.supplier.integration;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.cetelem.cassiope.supplier.io.f150.F150;
import ru.cetelem.cassiope.supplier.io.f150.F150Item;
import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.cassiope.supplier.model.PayloadItem;
import ru.cetelem.supplier.service.CarService;
import ru.cetelem.supplier.service.PayloadService;
import ru.cetelem.cassiope.supplier.util.DateUtils;


public class OutF150PayloadItemProcessor implements AbstractPayloadItemProcessor, AbstractCarPayloadItemUpdateProcessor {
	private static final Log log = LogFactory.getLog(OutF150PayloadItemProcessor.class);
	
	private CarService carService;
	private Payload payload;
	private PayloadService payloadService;
	
	OutF150PayloadItemProcessor(CarService carService, PayloadService payloadService, Payload payload){
		this.carService = carService;
		this.payload = payload;
		this.payloadService = payloadService;
	}
	
	@Override
	public void  process() {
		process(carService.findByState("SUBMITTED CFL"));

	}


	@Override
	public void process(List<Car> cars) {
		log.info("OutF150PayloadItemProcessor process list started ");
		
		if(this.payload.payloadItems.size()>0) {
			log.info("payloadItems not empty. clear");
			this.payload.payloadItems.stream().filter(pi->pi.car!=null).forEach(pi->pi.car.getPayloadItems().remove(pi));
			this.payload.payloadItems.clear();
		}
		
		
		F150.F150Builder cb = F150.F150Builder.createF150Builder(DateUtils.asDate(payload.date), 
				payload.sequenceNumber);

		
		carService.findByState("SUBMITTED CFL").stream().filter(car->cars.contains(car)).
			forEach(car->
				cb.addF150Item("RU", "6", car.getDealer().getCode(), car.getVin(), new Date(), "MOW1", 
					car.getEptsNumber(), car));

		createPayloadItems(cb.build().getAsList());
	}


	private void createPayloadItems(List<Object> fileRows) {
		for (Object fileItem : fileRows /* Create filerows and return them as a list */ ) {
			PayloadItem item =  payloadService.createPayloadItemByObject(payload, fileItem);
			
			if (fileItem instanceof F150Item) {
				F150Item f150Item = (F150Item)fileItem;
				item.eventCode = "F150";
				item.car =  f150Item.car;
				item.car.getPayloadItems().add(item);
				
			}

		}		
		
	}
}
