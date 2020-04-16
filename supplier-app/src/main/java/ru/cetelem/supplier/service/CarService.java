package ru.cetelem.supplier.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.cassiope.supplier.model.CarModel;
import ru.cetelem.cassiope.supplier.model.Dealer;
import ru.cetelem.cassiope.supplier.model.FinancePlan;
import ru.cetelem.cassiope.supplier.model.RepaymentItem;
import ru.cetelem.cassiope.supplier.util.DateUtils;
import ru.cetelem.supplier.repository.CarRepository;

@Service
@Transactional
public class CarService {
	private static final Log log = LogFactory.getLog(CarService.class); 

	private DictionaryService dictionaryService;
	private CarRepository carRepository;
	private DealerService dealerService;
	
	@Autowired
	private Environment environment;
	
	List<Car> cars;
	
	CarService(@Autowired DictionaryService dictionaryService,
			@Autowired DealerService dealerService,
			@Autowired CarRepository carRepository
			){
		this.dictionaryService = dictionaryService;
		this.carRepository = carRepository;
		this.dealerService = dealerService;

	}

	public List<Car> getCarsWithoutArchive() {
		log.info("CarService getCarsWithoutArchive started");
		
		Iterable<Car> iterable =  carRepository.findAllWithoutArchive();
		List<Car> models = StreamSupport.stream(iterable.spliterator(), false) .collect(Collectors.toList()); 

		log.info("CarService getCarsWithoutArchive finished");
		return models;

	}

	public List<Car> findInArchive(String vin, LocalDate dateFrom, 
			LocalDate dateTo, String dealer ){

		log.info("CarService findInArchive started");
		
		List<Car> models =  carRepository.findInArchive(vin, dateFrom, dateTo, dealer);


		log.info("CarService findInArchive finished");
		return models;
		
	}

		
	public List<Car> getCars() {
		log.info("CarService getCars started");
		
		Iterable<Car> iterable =  carRepository.findAll();
		List<Car> models = StreamSupport.stream(iterable.spliterator(), false) .collect(Collectors.toList()); 

		log.info("CarService getCars finished");
		return models;

	}
	
	public Optional<Car> findCarByVin(String vin) {
		log.info("CarService findCarByVin started");
		return carRepository.findByVin(vin);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Car saveCar(Car car) {
		log.info(String.format("CarService saveCar started for vin:  %s", car.getVin()));
		
		car = CarStateProcessor.build(carRepository, car).proccess();

		carRepository.save(car);
		
		log.info("CarService saveCar finished");
		return car;
	}

	public List<Car> findByState(String state) {
		log.info("CarService findByState started for state: " + state);
		
		return carRepository.findByState(state);
	}
	
	public RepaymentItem createRepaymentByPayloadItem(Car car, LocalDate repaymentDate, double value) {
		RepaymentItem ri = new RepaymentItem();

		ri.date =  repaymentDate;
		ri.value =  value;
		ri.car = car;
		car.getRepaymentItems().add(ri);
		

		return ri;
	}
	public DealerService getDealerService() {
		return dealerService;
	}
	public void setDealerService(DealerService dealerService) {
		this.dealerService = dealerService;
	}

	public Car newCar(String state, String vin, String model, String dealerCode, String invoiceNum, 
			double value, double valueFinance, String planCode, Date financeDate, Date whosaleDate,
			String eptsNumber) {
		log.info("CarService newCar started");
		if (vin == null || findCarByVin(vin).isPresent()) {
			log.info("CarService newCar ERROR: can`t create new car with vin=" + vin);
			return null;
		}
		
		if (model == null || "".equals(model.trim())) {
			log.info(String.format("parsing %s to get a model", vin));
			model = vin.substring(6, 10); // Get model from vin
		}

		Car car = new Car();
		car.setState(state);
		car.setVin(vin);
		car.setCarModel(getCarModel(model));
		car.setDealer(getDealer(dealerCode));
		car.setInvoiceNum(invoiceNum);
		car.setValue(value);
		car.setValueFinance(valueFinance);
		car.setFinancePlan(getFinancePlan(planCode));
		car.setEptsNumber(eptsNumber);		
		
		if (financeDate != null) {
			car.setFinanceDate(new java.sql.Date(financeDate.getTime()).toLocalDate());
		} else {
			car.setFinanceDate(null);
		}
		
		if (whosaleDate != null) {
			car.setIssueDate(new java.sql.Date(whosaleDate.getTime()).toLocalDate());
		} else {
			car.setIssueDate(LocalDate.now());
		}		
		
		saveCar(car);
		log.info("CarService newCar ended");
		return car;
	}

	private FinancePlan getFinancePlan(String planCode) {
		FinancePlan financePlan; 
		Optional<FinancePlan> financePlanOptional = dictionaryService.financePlanRepository.findByCode(planCode);
		if (financePlanOptional.isPresent()) {
			financePlan = financePlanOptional.get(); 
		} else {
			financePlan = new FinancePlan();
			financePlan.setCode(planCode);
			financePlan.setName(planCode);
			dictionaryService.financePlanAdd(financePlan);
		}
		return financePlan;
	}

	private Dealer getDealer(String dealerCode) {
		Dealer dealer; 
		Optional<Dealer> dealerOptional = dictionaryService.dealerRepository.findByCode(dealerCode);
		if (dealerOptional.isPresent()) {
			dealer = dealerOptional.get(); 
		} else {
			dealer = new Dealer();
			dealer.setCode(dealerCode);
			dealer.setName(dealerCode);
			dictionaryService.dealerAdd(dealer);
		}
		return dealer;
	}

	private CarModel getCarModel(String model) {
		CarModel carModel; 
		Optional<CarModel> carModelOptional = dictionaryService.carModelRepository.findByCode(model);
		if (carModelOptional.isPresent()) {
			carModel = carModelOptional.get(); 
		} else {
			carModel = new CarModel();
			carModel.setCode(model);
			carModel.setName(model);
			dictionaryService.carModelAdd(carModel);
		}
		return carModel;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int archiveCars(Set<Car> cars, LocalDate archiveDate) {
		int count = 0;
		for(Car car : cars) {
			if(car.getArchivedDate() == null) {
				car.setArchivedDate(archiveDate);
				saveCar(car);
				count++;
			}
		}
		return count;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int archiveCars(Set<Car> cars, LocalDate archiveDate, int days) {
		int count = 0;
		for(Car car : cars) {
			if(car.getArchivedDate() == null 
					&& car.getFullRepaymentDate() != null
					&& "REPAID".equals(car.getState())
			) {
				long daysBetween = ChronoUnit.DAYS.between(
						car.getFullRepaymentDate(), 
						DateUtils.asLocalDateTime(archiveDate));
				if(daysBetween >= days) {
					car.setArchivedDate(archiveDate);
					saveCar(car);
					count++;
				}
			}
		}
		return count;
	}

	public Environment getEnvironment() {
		return environment;
	}

}
