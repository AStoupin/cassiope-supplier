package ru.cetelem.supplier.integration;

//
// For Payloads with PayloadType.direction = IN 
//						process PaloadItems and load to Cassiope model
// For Payloads with PayloadType.direction = OUT 
//						process Cassiope model and load to PaloadItems  
public interface AbstractPayloadItemProcessor {
	void process();
}
