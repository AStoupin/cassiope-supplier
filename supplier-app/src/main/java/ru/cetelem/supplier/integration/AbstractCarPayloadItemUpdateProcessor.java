package ru.cetelem.supplier.integration;

import java.util.List;

import ru.cetelem.cassiope.supplier.model.Car;

//
//	reload (update) Payload by specified cars
//
public interface AbstractCarPayloadItemUpdateProcessor {
	void process(List<Car> cars);
}
