package ru.cetelem.supplier.integration;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.cetelem.cassiope.supplier.io.icfl.ICfl22Item;
import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.cassiope.supplier.model.PayloadItem;
import ru.cetelem.supplier.service.CarService;
import ru.cetelem.supplier.service.PayloadService;

public class InICflPayloadItemProcessor implements AbstractPayloadItemProcessor {
	private static final Log log = LogFactory
			.getLog(InICflPayloadItemProcessor.class);

	private CarService carService;
	private Payload payload;
	private PayloadService payloadService;

	InICflPayloadItemProcessor(CarService carService,
			PayloadService payloadService, Payload payload) {
		this.carService = carService;
		this.payload = payload;
		this.payloadService = payloadService;
	}

	@Override
	public void process() {
		log.info("InICflPayloadItemProcessor process started");

		for (PayloadItem payloadItem : payload.payloadItems) {
			if (payloadItem.sourceType.endsWith("ICflHeader")) {
				payloadItem.errorDescr = "OK";
			} else if (payloadItem.sourceType.endsWith("ICflTrail")) {
				payloadItem.errorDescr = "OK";
			} else if (payloadItem.sourceType.endsWith("ICfl11Trail")) {
				payloadItem.errorDescr = "OK";
			} else if (payloadItem.sourceType.endsWith("ICfl22Trail")) {
				payloadItem.errorDescr = "OK";
			} else if (payloadItem.sourceType.endsWith("ICfl33Trail")) {
				payloadItem.errorDescr = "OK";
			} else if (payloadItem.sourceType.endsWith("ICfl44Trail")) {
				payloadItem.errorDescr = "OK";
			} else if (payloadItem.sourceType.endsWith("ICfl11Item")) {
				payloadItem.errorDescr = "OK";
//			} else if (payloadItem.sourceType.endsWith("ICfl11Item")) {
//				processICfl11PayloadItem(payloadItem);
			} else if (payloadItem.sourceType.endsWith("ICfl22Item")) {
				processICfl22PayloadItem(payloadItem);

			} else {
				payloadItem.errorDescr = "Unkown sourceType";
			}

		}

		log.info("InICflPayloadItemProcessor process finished");
	}

	private void processICfl22PayloadItem(PayloadItem payloadItem) {
		ICfl22Item icflItem = (ICfl22Item) payloadService
				.createObjectByPayloadItem(payloadItem);
		Optional<Car> car = carService.findCarByVin(icflItem.vin);

		if (!car.isPresent()) {
			carService.newCar("ICFL", icflItem.vin, icflItem.model, 
				icflItem.dealerCode, icflItem.invoiceNumber, 
				(double) (icflItem.invoiceAmount / 100.0), 
				(double) (icflItem.paymentAmount / 100.0), 
				icflItem.planCode, icflItem.financeDate, icflItem.whosaleDate, "" /* eptsNumber */ );
			car = carService.findCarByVin(icflItem.vin);
		}

		// Some checks: vin exists, car is't repayed
		if (!car.isPresent()) {
			String error = String.format("Vin = %s not found", icflItem.vin);
			payloadItem.errorDescr = error;
			log.info(error);

			return;
		}

		// carService.createRepaymentByPayloadItem(car.get(), new java.sql.Date(
		// icflItem.financingDate.getTime()).toLocalDate(),
		// icflItem.invoiceAmount / 100);
		payloadItem.eventCode = "ICFL";
		payloadItem.car = car.get();
		payloadItem.car.getPayloadItems().add(payloadItem);

		payloadItem.errorDescr = "OK";
	}

}
