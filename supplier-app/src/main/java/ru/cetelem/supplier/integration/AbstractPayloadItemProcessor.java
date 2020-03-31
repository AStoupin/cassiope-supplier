package ru.cetelem.supplier.integration;

import java.util.List;

import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.cassiope.supplier.model.PayloadItem;

//
// For Payloads with PayloadType.direction = IN 
//						process PaloadItems and load to Cassiope model
// For Payloads with PayloadType.direction = OUT 
//						process Cassiope model and load to PaloadItems  
public interface AbstractPayloadItemProcessor {
	void process();
}
