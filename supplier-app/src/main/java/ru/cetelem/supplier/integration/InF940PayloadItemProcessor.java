package ru.cetelem.supplier.integration;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.cetelem.cassiope.supplier.io.f940.F940Item;
import ru.cetelem.cassiope.supplier.model.Dealer;
import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.cassiope.supplier.model.PayloadItem;
import ru.cetelem.supplier.service.CarService;
import ru.cetelem.supplier.service.PayloadService;

public class InF940PayloadItemProcessor implements AbstractPayloadItemProcessor {
	private static final Log log = LogFactory
			.getLog(InF940PayloadItemProcessor.class);

	private CarService carService;
	private Payload payload;
	private PayloadService payloadService;

	InF940PayloadItemProcessor(CarService carService,
			PayloadService payloadService, Payload payload) {
		this.carService = carService;
		this.payload = payload;
		this.payloadService = payloadService;
	}

	@Override
	public void process() {
		log.info("InF940PayloadItemProcessor process started");

		for (PayloadItem payloadItem : payload.payloadItems) {
			if (payloadItem.sourceType.endsWith("F940Header")) {
				payloadItem.errorDescr = "OK";
			} else if (payloadItem.sourceType.endsWith("F940Trail")) {
				payloadItem.errorDescr = "OK";
			} else if (payloadItem.sourceType.endsWith("F940Item")) {
				processRepaymentPayloadItem(payloadItem);

			} else {
				payloadItem.errorDescr = "Unkown sourceType";
			}

		}

		log.info("InF940PayloadItemProcessor process finished");
	}

	private void processRepaymentPayloadItem(PayloadItem payloadItem) {
		F940Item f940Item = (F940Item) payloadService
				.createObjectByPayloadItem(payloadItem);
		Optional<Dealer> dealer = carService.getDealerService().findByCode(
				f940Item.dealerCode);

		// Some checks: dealerCode exists, dealer not found
		if (!dealer.isPresent()) {
			String error = String.format("dealer = %s not found", f940Item.dealerCode);
			payloadItem.errorDescr = error;
			log.info(error);

			return;
		}

		carService.getDealerService().updateDealerLimit(
				dealer.get().getLimit(), f940Item.hardLimitATLAS / 100,
				f940Item.softLimitATLAS / 100, f940Item.totalFinanced / 100,
				f940Item.amountAvailable / 100, f940Item.sublimit_DE_CC / 100,
				f940Item.totalFinanced_DE_CC / 100,
				f940Item.amountAvailable_DE_CC / 100);

		payloadItem.eventCode = "940";
		payloadItem.limit = dealer.get().getLimit();
		payloadItem.limit.getPayloadItems().add(payloadItem);

		payloadItem.errorDescr = "OK";
	}

}
