package ru.cetelem.supplier.integration;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.cetelem.cassiope.supplier.io.FileEntry;
import ru.cetelem.cassiope.supplier.io.PayloadType;
import ru.cetelem.cassiope.supplier.io.f920.F920Header;
import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.supplier.service.DictionaryService;
import ru.cetelem.supplier.service.PayloadService;
import static java.lang.String.*;

@Component
public  class FileInProcessor implements Processor{
		private static final Log log = LogFactory.getLog(FileInProcessor.class);

		@SuppressWarnings("unused")
		private DictionaryService dictionaryService;
		private PayloadService payloadService;
	
	
	FileInProcessor(@Autowired PayloadService payloadService,
			@Autowired DictionaryService dictionaryService){
		this.payloadService = payloadService;
		this.dictionaryService = dictionaryService;
	}

	
	
	@Override
	public void process(Exchange exchange) throws Exception {
		log.info("process started");
		
		@SuppressWarnings("unchecked")
		List<FileEntry> list = (List<FileEntry>) exchange.getIn().getBody();
		@SuppressWarnings("unchecked")
		String fileName = ((org.apache.camel.component.file.GenericFile<Object>) 
						exchange.getProperties().get("CamelFileExchangeFile")).getFileName();  
		
		Payload payload = recievePayload(list, fileName);
		if (list.size() > 0) {
			FileEntry fileEntry = list.get(0);
			if (fileEntry instanceof F920Header) {
				// TODO Можно ли хранить в (int)payload.sequenceNumber значение (long)F920Header.fileNumber?
				payload.sequenceNumber = (int) ((F920Header) fileEntry).fileNumber;
			}
		}
		
		payloadService.proccessInputPayloadItemsFor(payload);
		
		if("FAILED".equals(payload.state))
			throw new RuntimeException(String.format("File %s processed with errors", fileName));
		
		log.info(String.format("process finished for %d items", list.size()));
	}

	
	private Payload recievePayload(List<FileEntry> list, String fileName) {
		log.info("recievePayload started");

		Payload payload = new Payload();
		payload.name = fileName;

		for (FileEntry entry : list) {
			PayloadType payloadType = entry.getType();
			if (payloadType == null) throw new NullPointerException(format(
			    "entry '%s' has no payload type",
			    entry));
			if (payload.getPayloadType() == null) {
				payload.setPayloadType(payloadType);
			} else if (payload.getPayloadType() != entry.getType()) {
				throw new IllegalStateException(format(
				    "Mixed payload types in single payload: %s %s",
				    payload.getPayloadType(), payloadType));
			}
			payloadService.createPayloadItemByObject(payload, entry);
		}

		payloadService.savePayload(payload);

		return payload;
	}			
	
}
