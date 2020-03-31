package ru.cetelem.supplier.integration;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.supplier.service.PayloadService;

@Component
public class FileOutProcessor implements  Processor{

	private static final Log log = LogFactory.getLog(FileOutProcessor.class);
	
	protected PayloadService payloadService;

	public FileOutProcessor(@Autowired PayloadService payloadService) {
		super();
		this. payloadService = payloadService;
	}


	
	@Override
	public void process(Exchange exchange) throws Exception {
		log.debug(String.format("process started for exchange %s", exchange.getExchangeId() ));
		
		
		List<Payload> payloads = payloadService.getPayloadByStatus("SUBMITTED");

		if(payloads.size()>0) {
			log.info("Found Payload to proccess");
			
			Payload payload = payloads.get(0);
			exchange.getProperties().put("CamelFileHost", payload.name);
			exchange.getIn().setBody(getItems(payload));

		}
			
		log.debug("process finished");
	}

	public List<Object> getItems(Payload payload) {
		
		List<Object> result = payload.payloadItems.stream().map(payloadService::createObjectByPayloadItem)
				.collect(Collectors.toList());

		return result;
	}

	public void setProcessed(Exchange exchange) throws Exception {
		log.info(String.format("setProcessed started for exchange %s", exchange.getExchangeId() ));
		
		Optional<Payload> payload = payloadService.getPayloadByName(exchange.getProperties().get("CamelFileHost").toString());
		if(payload.isPresent()) {
			payload.get().processedDate = LocalDate.now();
			payloadService.savePayload(payload.get());
			log.info(String.format("Payload status for %s has PROCESSED", payload.get().name));
		}
		log.info("setProcessed finished");
	}

}