package ru.cetelem.supplier.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import ru.cetelem.cassiope.supplier.io.PayloadType;
import ru.cetelem.cassiope.supplier.model.CarModel;
import ru.cetelem.cassiope.supplier.model.Dealer;
import ru.cetelem.cassiope.supplier.model.FinancePlan;
import ru.cetelem.supplier.repository.CarModelRepository;
import ru.cetelem.supplier.repository.DealerRepository;
import ru.cetelem.supplier.repository.FinancePlanRepository;


@Service
public class DictionaryService {
	private static final Log log = LogFactory.getLog(DictionaryService.class); 

	public CarModelRepository carModelRepository;
	public FinancePlanRepository financePlanRepository;
	public DealerRepository dealerRepository;
	
	@Autowired
	private Environment environment;
	
	DictionaryService(@Autowired CarModelRepository carModelRepository,
			@Autowired FinancePlanRepository financePlanRepository,
			@Autowired DealerRepository dealerRepository
			){
		this.carModelRepository = carModelRepository;
		this.financePlanRepository = financePlanRepository;
		this.dealerRepository = dealerRepository;
	}

	public List<CarModel> getModels(){
		log.info("getModels started");
		
		Iterable<CarModel> iterable =  carModelRepository.findAll();
		List<CarModel> models = StreamSupport.stream(iterable.spliterator(), false) .collect(Collectors.toList()); 

		log.info("getModels finished");
		return models;
	}
	
	public CarModel getModelById(int id) {
		log.info("getModelById started");
		
		return carModelRepository.findById(id).get();
	}

	public CarModel carModelUpdate(CarModel carModel) {
		log.info("carModelUpdate started");

		carModelRepository.save(carModel);
		
		return carModel;
	}
	
	public CarModel carModelAdd(CarModel carModel) {
		log.info("carModelAdd started");

		carModelRepository.save(carModel);

		return carModel;
		
	}
	
	public void carModelDelete(CarModel carModel) {
		log.info("carModelDelete started");
	
		carModelRepository.delete(carModel);
		return ;
	}
	
	public Dealer dealerAdd(Dealer dealer) {
		log.info("dealerAdd started");

		dealerRepository.save(dealer);

		return dealer;		
	}
	
	public FinancePlan financePlanAdd(FinancePlan financePlan) {
		log.info("financePlanAdd started");

		financePlanRepository.save(financePlan);

		return financePlan;		
	}
	
	public List<FinancePlan> getFinancePlanes() {
		log.info("getFinancePlanes started");
		
		Iterable<FinancePlan> iterable =  financePlanRepository.findAll();
		List<FinancePlan> financePlanes = StreamSupport.stream(iterable.spliterator(), false) .collect(Collectors.toList()); 
		
		log.info("getFinancePlanes finished");

		return financePlanes;
	}
	
	public List<PayloadType> getPayloadTypes(){
		log.info("getPayloadTypes started");
		
		return Arrays.asList(PayloadType.values());
	}

	public Environment getEnvironment() {
		return environment;
	}
}
