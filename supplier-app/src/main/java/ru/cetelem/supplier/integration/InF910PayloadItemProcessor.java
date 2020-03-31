package ru.cetelem.supplier.integration;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.cetelem.cassiope.supplier.io.f910.F910Item;
import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.cassiope.supplier.model.PayloadItem;
import ru.cetelem.supplier.service.CarService;
import ru.cetelem.supplier.service.PayloadService;

public class InF910PayloadItemProcessor implements AbstractPayloadItemProcessor {
	private static final Log log = LogFactory
			.getLog(InF910PayloadItemProcessor.class);

	private CarService carService;
	private Payload payload;
	private PayloadService payloadService;

	InF910PayloadItemProcessor(CarService carService,
			PayloadService payloadService, Payload payload) {
		this.carService = carService;
		this.payload = payload;
		this.payloadService = payloadService;
	}

	@Override
	public void process() {
		log.info("InF910PayloadItemProcessor process started");
		
		for (PayloadItem payloadItem : payload.payloadItems) {
			if (payloadItem.sourceType.endsWith("F910Header")) {
				payloadItem.errorDescr = "OK";
			} else if (payloadItem.sourceType.endsWith("F910Trail")) {
				payloadItem.errorDescr = "OK";
			} else if (payloadItem.sourceType.endsWith("F910Item")) {
				processFinancedPayloadItem(payloadItem);

			} else {
				payloadItem.errorDescr = "Unkown sourceType";
			}

		}

		log.info("InF910PayloadItemProcessor process finished");
	}

	private void processFinancedPayloadItem(PayloadItem payloadItem) {
		F910Item f910Item = (F910Item) payloadService
				.createObjectByPayloadItem(payloadItem);
		Optional<Car> car = carService.findCarByVin(f910Item.vin);

		// Some checks: vin exists, car is't repayed
		if (!car.isPresent()) {
			String error = String.format("Vin = %s not found", f910Item.vin);
			payloadItem.errorDescr = error;
			log.info(error);

			return;
		}

//		carService.createRepaymentByPayloadItem(car.get(), new java.sql.Date(
//				f910Item.financingDate.getTime()).toLocalDate(),
//				f910Item.invoiceAmount / 100);
		payloadItem.eventCode = "910";
		payloadItem.car = car.get();
		payloadItem.car.getPayloadItems().add(payloadItem);

		payloadItem.errorDescr = "OK";
	}

}
