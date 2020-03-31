package ru.cetelem.supplier.integration;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.cetelem.cassiope.supplier.io.f920.F920Item;
import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.cassiope.supplier.model.PayloadItem;
import ru.cetelem.supplier.service.CarService;
import ru.cetelem.supplier.service.PayloadService;


public class InF920PayloadItemProcessor implements AbstractPayloadItemProcessor {
	private static final Log log = LogFactory.getLog(InF920PayloadItemProcessor.class);
	
	private CarService carService;
	private Payload payload;
	private PayloadService payloadService;
	
	InF920PayloadItemProcessor(CarService carService, PayloadService payloadService, Payload payload){
		this.carService = carService;
		this.payload = payload;
		this.payloadService = payloadService;
	}
	
	@Override
	public void  process() {
		log.info("InF920PayloadItemProcessor process started");
	
		for (PayloadItem payloadItem : payload.payloadItems) {
			if(payloadItem.sourceType.endsWith("F920Header")) {
				int fileNumber = payloadService.generatePayloadSequence(payload.payloadType);
				// TODO Можно ли хранить в payload.sequenceNumber значение F920Header.fileNumber?
				if (fileNumber <= payload.sequenceNumber) {
					payloadItem.errorDescr = "OK";
				} else {
					payloadItem.errorDescr = "[ERROR: old fileNumber=" + (fileNumber - 1) 
							+ " and new fileNumber=" + payload.sequenceNumber + "]";
					payloadItem.payload.state = "FAILED";
					log.info("InF920PayloadItemProcessor process. " + payloadItem.errorDescr);
				}
			}
			else if (payloadItem.sourceType.endsWith("F920Trail")) {
				payloadItem.errorDescr = "OK";
			}
			else if (payloadItem.sourceType.endsWith("F920Item")) {
				processRepaymentPayloadItem(payloadItem);
		
			}
			else {
				payloadItem.errorDescr = "Unknown sourceType";
			}
		
		}
		log.info("InF920PayloadItemProcessor process finished");
	}

	private void processRepaymentPayloadItem(PayloadItem payloadItem) {
		F920Item f920Item = (F920Item) payloadService.createObjectByPayloadItem(payloadItem);
		Optional<Car> car = carService.findCarByVin(f920Item.vin);


		// Some checks: vin exists, car is't repayed
		if(!car.isPresent()) {
			String error = String.format("Vin = %s not found", f920Item.vin);
			payloadItem.errorDescr = error;
			log.info(error);
			
			return;
		}				
		
		
		
		carService.createRepaymentByPayloadItem(car.get(),
				new java.sql.Date(f920Item.valueDate.getTime()).toLocalDate(),
				f920Item.amount / 100
				);
		payloadItem.eventCode = "920";
		payloadItem.car = car.get();
		payloadItem.car.getPayloadItems().add(payloadItem);
		
		payloadItem.errorDescr = "OK";		
	}



}
