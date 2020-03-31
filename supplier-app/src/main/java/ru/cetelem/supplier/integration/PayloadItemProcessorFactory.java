package ru.cetelem.supplier.integration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.Environment;

import ru.cetelem.cassiope.supplier.io.PayloadType;
import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.supplier.service.CarService;
import ru.cetelem.supplier.service.PayloadService;

public class PayloadItemProcessorFactory {
	private static final Log log = LogFactory.getLog(PayloadItemProcessorFactory.class);

	private CarService carService;
	private PayloadService payloadService;

	public PayloadItemProcessorFactory(CarService carService, PayloadService payloadService) {
		this.carService = carService;
		this.payloadService = payloadService;
	}

	public AbstractPayloadItemProcessor build(Payload payload, Environment environment) {
		log.info("AbstractPayloadItemProcessor build started, payload = " + payload);
		
		PayloadType payloadType = payload.getPayloadType();
		log.info(String.format("build for type %s", payloadType));
		switch (payloadType) {
		case CFL:
			return new OutCflPayloadItemProcessor(carService, payloadService, payload, environment);
		case ICFL:
			return new InICflPayloadItemProcessor(carService, payloadService, payload);
		case F120:
			return new OutF120PayloadItemProcessor(carService, payloadService, payload);
		case F150:
			return new OutF150PayloadItemProcessor(carService, payloadService, payload);
		case F910:
			return new InF910PayloadItemProcessor(carService, payloadService, payload);
		case F920:
			return new InF920PayloadItemProcessor(carService, payloadService, payload);
		case F940:
			return new InF940PayloadItemProcessor(carService, payloadService, payload);
		case F950:
			return new InF950PayloadItemProcessor(carService, payloadService, payload);
		default:
			throw new RuntimeException(String.format("Incorrect type %s", payloadType));
		}
	}

}
