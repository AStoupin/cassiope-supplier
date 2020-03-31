package ru.cetelem.supplier.integration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.cetelem.cassiope.supplier.io.f950.F950Item;
import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.cassiope.supplier.model.PayloadItem;
import ru.cetelem.supplier.service.CarService;
import ru.cetelem.supplier.service.PayloadService;

public class InF950PayloadItemProcessor implements AbstractPayloadItemProcessor {
	private static final Log log = LogFactory.getLog(InF950PayloadItemProcessor.class);

	private CarService carService;
	private Payload payload;
	private PayloadService payloadService;

	InF950PayloadItemProcessor(CarService carService, PayloadService payloadService, Payload payload) {
		this.carService = carService;
		this.payload = payload;
		this.payloadService = payloadService;
	}

	@Override
	public void process() {
		log.info("InF950PayloadItemProcessor process started");

		for (PayloadItem payloadItem : payload.payloadItems) {
			if (payloadItem.sourceType.endsWith("F950Header")) {
				payloadItem.errorDescr = "OK";
			} else if (payloadItem.sourceType.endsWith("F950Trailer")) {
				payloadItem.errorDescr = "OK";
			} else if (payloadItem.sourceType.endsWith("F950Item")) {
				processRepaymentPayloadItem(payloadItem);
			} else {
				payloadItem.errorDescr = "Unkown sourceType";
			}
		}

		log.info("InF950PayloadItemProcessor process finished");
	}

	private void processRepaymentPayloadItem(PayloadItem payloadItem) {
		F950Item f950Item = (F950Item) payloadService.createObjectByPayloadItem(payloadItem);
		Car car = carService.findCarByVin(f950Item.vin).orElse(null);

		if (car == null) {
			String error = String.format("Vin = %s not found", f950Item.vin);
			payloadItem.errorDescr = error;
			log.info(error);
			return;
		}

		payloadItem.eventCode = "950";
		payloadItem.car = car;
		payloadItem.car.getPayloadItems().add(payloadItem);

		payloadItem.errorDescr = "OK";
	}

}
