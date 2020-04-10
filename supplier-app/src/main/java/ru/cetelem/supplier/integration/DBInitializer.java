package ru.cetelem.supplier.integration;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.cassiope.supplier.model.CarModel;
import ru.cetelem.cassiope.supplier.model.Dealer;
import ru.cetelem.cassiope.supplier.model.FinancePlan;
import ru.cetelem.supplier.repository.CarModelRepository;
import ru.cetelem.supplier.repository.CarRepository;
import ru.cetelem.supplier.repository.DealerLimitRepository;
import ru.cetelem.supplier.repository.DealerRepository;
import ru.cetelem.supplier.repository.FinancePlanRepository;
import ru.cetelem.supplier.repository.PayloadItemRepository;
import ru.cetelem.supplier.repository.PayloadRepository;

@Component
@SuppressWarnings("unused")
public class DBInitializer implements InitializingBean {

    private static final Logger log = LogManager.getLogger(DBInitializer.class);

    private final CarModelRepository carModelRepository;
    private final FinancePlanRepository financePlanRepository;
	private final DealerLimitRepository dealerLimitRepository;
    private final DealerRepository dealerRepository;
    private final CarRepository carRepository;
    private final PayloadRepository payloadRepository;
    private final PayloadItemRepository payloadItemRepository;

    @Autowired
    public DBInitializer(
            CarModelRepository carModelRepository,
            FinancePlanRepository financePlanRepository,
            DealerLimitRepository dealerLimitRepository,
            DealerRepository dealerRepository,
            CarRepository carRepository,
            PayloadRepository payloadRepository,
            PayloadItemRepository payloadItemRepository
    ) {
        this.carModelRepository = carModelRepository;
        this.financePlanRepository = financePlanRepository;
        this.dealerLimitRepository = dealerLimitRepository;
        this.dealerRepository = dealerRepository;
        this.carRepository = carRepository;
        this.payloadRepository = payloadRepository;
        this.payloadItemRepository = payloadItemRepository;
    }

    @Override
    public void afterPropertiesSet() {
    	if(!carModelRepository.findByCode("TTGY").isPresent()) {
	        initCarModels();
	        initPlanCodes();
	        initDealers();
	        initCars();
	        initPayloads();
    	}
    }

    private void initCarModels() {
        log.trace("initializing car models...");
        carModelRepository.save(new CarModel(2, "TTGY", "Transit"));
        carModelRepository.save(new CarModel(3, "ESMA", "Kuga"));
        log.trace("initializing car models done");
    }

    private void initPlanCodes() {
        log.trace("initializing plan codes...");
        financePlanRepository.save(new FinancePlan("TL", "Local origin"));
        financePlanRepository.save(new FinancePlan("TU", "Unconsented trading"));
        financePlanRepository.save(new FinancePlan("DE", "Demo"));
        financePlanRepository.save(new FinancePlan("CC", "Courtesy"));
        financePlanRepository.save(new FinancePlan("FL", "Fleet"));
        log.trace("initializing plan codes done");
    }

    private void initDealers() {
        log.trace("initializing dealers...");
        dealerRepository.save(new Dealer("00238", "Sura Motors Avto (Penza)"));
        dealerRepository.save(new Dealer("00138", "Atlant-M (Smolensk)"));
        dealerRepository.save(new Dealer("00310", "Kuntsevo Avto LLC"));
        log.trace("initializing dealers done");
    }

    private void initCars() {
        log.info("initializing cars...");

        CarModel model_TTGY = carModelRepository.findByCode("TTGY").get();
        CarModel model_ESMA = carModelRepository.findByCode("ESMA").get();

        Dealer dealer_00238 = dealerRepository.findByCode("00238").get();
        Dealer dealer_00138 = dealerRepository.findByCode("00138").get();
        Dealer dealer_00310 = dealerRepository.findByCode("00310").get();

        FinancePlan planCode_TL = financePlanRepository.findByCode("TL").get();
        FinancePlan planCode_TU = financePlanRepository.findByCode("TU").get();
        FinancePlan planCode_DE = financePlanRepository.findByCode("DE").get();
        FinancePlan planCode_CC = financePlanRepository.findByCode("CC").get();
        FinancePlan planCode_FL = financePlanRepository.findByCode("FL").get();

        carRepository.save(newCar(
                "WF0YXXTTGYFT42745", 1750282.2, 1750282.2,
                "1220", LocalDate.now(), "NEW",
                model_TTGY, null, planCode_TL));
        carRepository.save(newCar(
                "Z6FAXXESMACT57438", 1282698.94, 1282698.94,
                "1221", LocalDate.now(), "NEW",
                model_ESMA, null, planCode_TU));
        carRepository.save(newCar(
                "WF0YXXTTGYFT42719", 1971325.70, 1971325.70,
                "1230", LocalDate.now(), "READY TO SUBMIT",
                model_TTGY, dealer_00310, planCode_DE));
        carRepository.save(newCar(
                "Z6FAXXESMADJ69961", 957653.78, 957653.78,
                "1231", LocalDate.now(), "READY TO SUBMIT",
                model_ESMA, dealer_00238, planCode_CC));
        carRepository.save(newCar(
                "WF0YXXTTGYFT41790", 1683131.94, 1683131.94,
                "1232", LocalDate.now(), "READY TO SUBMIT",
                model_TTGY, dealer_00238, planCode_FL));
        /*
        carRepository.save(newCar(
                "00000000000000005", 12345, 45678,
                "1232", LocalDate.now(), "READY TO FINANCE",
                model_TTGY, dealer_UR138, planCode_FL));
        carRepository.save(newCar(
                "00000000000000006", 12345, 45678,
                "1233", LocalDate.now(), "READY TO FINANCE",
                model_ESMA, dealer_UR310, planCode_TL));
        carRepository.save(newCar(
                "00000000000000007", 12345, 45678,
                "1234", LocalDate.now(), "FINANCE REQUESTED",
                model_TTGY, dealer_UR238, planCode_TU));
        carRepository.save(newCar(
                "00000000000000008", 12345, 45678,
                "1235", LocalDate.now(), "FINANCE REQUESTED",
                model_ESMA, dealer_UR138, planCode_DE));
        carRepository.save(newCar(
                "00000000000000009", 12345, 45678,
                "1236", LocalDate.now(), "FINANCE REQUESTED",
                model_TTGY, dealer_UR310, planCode_CC));
        carRepository.save(newCar(
                "00000000000000010", 12345, 45678,
                "1237", LocalDate.now(), "SUBMITTED",
                model_ESMA, dealer_UR238, planCode_FL));
        carRepository.save(newCar(
                "00000000000000011", 12345, 45678,
                "1238", LocalDate.now(), "SUBMITTED",
                model_TTGY, dealer_UR138, planCode_TL));
        carRepository.save(newCar(
                "00000000000000012", 12345, 45678,
                "1239", LocalDate.now(), "FINANCED",
                model_ESMA, dealer_UR310, planCode_TU));
        */
        log.info("initializing cars done");
    }

    private void initPayloads() {
        /*
        payloadRepository.save(new Payload(F120, "FileName2.txt", LocalDate.now()));
        payloadRepository.save(new Payload(F910, "F910_20190808.txt", LocalDate.now()));
        payloadRepository.save(new Payload(F950, "F950_20190812.txt", LocalDate.now()));
        payloadRepository.save(new Payload(F920, "F920_20190812.txt", LocalDate.now()));

        payloadItemRepository.save(new PayloadItem(
                payloadRepository.findByName("F910_20190808.txt").get(),
                carRepository.findByVin("00000000000000009").get(), "910"));
        payloadItemRepository.save(new PayloadItem(
                payloadRepository.findByName("F910_20190808.txt").get(),
                carRepository.findByVin("00000000000000010").get(), "910"));
        payloadItemRepository.save(new PayloadItem(
                payloadRepository.findByName("F950_20190812.txt").get(),
                carRepository.findByVin("00000000000000011").get(), "950"));
        payloadItemRepository.save(new PayloadItem(
                payloadRepository.findByName("F920_20190812.txt").get(),
                carRepository.findByVin("00000000000000012").get(), "920"));
        */
    }

    private Car newCar(
            String vin,
            double value,
            double valueFinance,
            String invoiceNum,
            LocalDate issueDate,
            String state,
            CarModel model,
            Dealer dealer,
            FinancePlan financePlan
    ) {
        Car car = new Car();
        car.setVin(vin);
        car.setValue(value);
        car.setValueFinance(valueFinance);
        car.setInvoiceNum(invoiceNum);
        car.setIssueDate(issueDate);
        car.setState(state);
        car.setCarModel(model);
        car.setDealer(dealer);
        car.setFinancePlan(financePlan);
        return car;
    }
}
