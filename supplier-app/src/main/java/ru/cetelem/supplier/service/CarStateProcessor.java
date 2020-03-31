package ru.cetelem.supplier.service;

import static ru.cetelem.cassiope.supplier.io.PayloadType.CFL;
import static ru.cetelem.cassiope.supplier.io.PayloadType.F120;
import static ru.cetelem.cassiope.supplier.io.PayloadType.F150;
import static ru.cetelem.cassiope.supplier.io.PayloadType.F910;
import static ru.cetelem.cassiope.supplier.io.PayloadType.F920;
import static ru.cetelem.cassiope.supplier.io.PayloadType.F950;
import static ru.cetelem.cassiope.supplier.io.PayloadType.ICFL;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.cassiope.supplier.model.PayloadItem;
import ru.cetelem.supplier.repository.CarRepository;

public class CarStateProcessor {
	private static final Log log = LogFactory.getLog(CarStateProcessor.class);
	
	//NEW-(USER)->READY TO SUBMIT-(CFL extracted)->SUBMITTED CFL-(F150 extracted)->SUBMITTED-(F950 imported)->
	//READY TO FINANCE-(F120 extracted)->FINANCE REQUESTED-(F910 imported)->FINANCED-(F920 imported)->REPAID
	private static final String CAR_STATE_NEW = "NEW";
	private static final String CAR_STATE_READY_TO_SUBMIT = "READY TO SUBMIT";
	private static final String CAR_STATE_SUBMITTED_CFL = "SUBMITTED CFL";
	private static final String CAR_STATE_SUBMITTED = "SUBMITTED";
	private static final String CAR_STATE_READY_TO_FINANCE = "READY TO FINANCE";
	private static final String CAR_STATE_FINANCE_REQUESTED = "FINANCE REQUESTED";
	private static final String CAR_STATE_FINANCED = "FINANCED";
	private static final String CAR_STATE_REPAID = "REPAID"; // FULL REPAYED
	private static final String CAR_STATE_CANCEL = "CANCEL";
	private static final String CAR_STATE_ARCHIVED = "ARCHIVED";
	private static final String CAR_STATE_ICFL = "ICFL";

	private StateProccess stateProccess;
	private Car carNew;
	private Car carOld;

	public Car proccess() {
		return stateProccess.proccess(carOld, carNew);
	}

	interface StateProccess {
		default Car proccess(Car carOld, Car carNew) {
			return carNew;
		}
	}

	public static class DefaultState implements StateProccess {
	}

	public static class NewState implements StateProccess {
		private static final Log log = LogFactory.getLog(NewState.class);

		@Override
		public Car proccess(Car carOld, Car carNew) {
			log.info("NewState process started");

			if (CAR_STATE_NEW.equals(carNew.getState())
					&& carNew.getInvoiceNum() != null
					&& !"".equals(carNew.getInvoiceNum())
					&& carNew.getDealer() != null
					&& carNew.getFinancePlan() != null) {
				carNew.setState(CAR_STATE_READY_TO_SUBMIT);
			}

			if (CAR_STATE_NEW.equals(carNew.getState())
					&& carNew.getArchivedDate() != null) {
				carNew.setState(CAR_STATE_ARCHIVED);
			}

			return carNew;
		}

	}

	public static class ReadyToSubmitState implements StateProccess {
		private static final Log log = LogFactory
				.getLog(ReadyToSubmitState.class);

		@Override
		public Car proccess(Car carOld, Car carNew) {
			log.info("ReadyToSubmitState process started");

			Optional<PayloadItem> submitPayloadItem = carNew
					.getPayloadItems()
					.stream()
					.filter(p -> p.getPayload().getPayloadType() == CFL
							&& "PROCESSED".equals(p.payload.state)
							&& "22".equals(p.eventCode)).findFirst();

			if (CAR_STATE_READY_TO_SUBMIT.equals(carNew.getState())
					&& carNew.getDealer() != null
					&& submitPayloadItem.isPresent()) {
				carNew.setState(CAR_STATE_SUBMITTED_CFL);
			} else if (carNew.getDealer() == null
					&& CAR_STATE_READY_TO_SUBMIT.equals(carNew.getState())) {
				carNew.setState(CAR_STATE_CANCEL);
			}

			return carNew;
		}

	}

	public static class SubmittedCflState implements StateProccess {
		private static final Log log = LogFactory
				.getLog(SubmittedCflState.class);

		@Override
		public Car proccess(Car carOld, Car carNew) {
			log.info("ReadyToSubmitCflState process started");

			Optional<PayloadItem> submitPayloadItem = carNew
					.getPayloadItems()
					.stream()
					.filter(p -> p.getPayload().getPayloadType() == F150
							&& (!"PROCESSED".equals(p.payload.state)))
					.findFirst();

			if (CAR_STATE_SUBMITTED_CFL.equals(carNew.getState())
					&& carNew.getDealer() != null
					&& submitPayloadItem.isPresent()) {
				carNew.setState(CAR_STATE_SUBMITTED);
			} else if (carNew.getDealer() == null
					&& CAR_STATE_SUBMITTED_CFL.equals(carNew.getState())) {
				carNew.setState(CAR_STATE_CANCEL);
			}

			return carNew;
		}

	}

	public static class CanceledState implements StateProccess {
		private static final Log log = LogFactory
				.getLog(ReadyToSubmitState.class);

		@Override
		public Car proccess(Car carOld, Car carNew) {
			log.info("CanceledState process started");

			Optional<PayloadItem> submitPayloadItem = carNew
					.getPayloadItems()
					.stream()
					.filter(p -> p.getPayload().getPayloadType() == CFL
							&& ("PROCESSED".equals(p.payload.state) || "SUBMITTED"
									.equals(p.payload.state))
							&& "11".equals(p.eventCode)).findFirst();

			if (CAR_STATE_CANCEL.equals(carNew.getState())
					&& submitPayloadItem.isPresent()) {
				carNew.setState(CAR_STATE_NEW);
			}

			return carNew;
		}

	}

	public static class SubmittedState implements StateProccess {
		private static final Log log = LogFactory.getLog(SubmittedState.class);

		@Override
		public Car proccess(Car carOld, Car carNew) {
			log.info("SubmittedState process started");

			Optional<PayloadItem> submitPayloadItem = carNew
					.getPayloadItems()
					.stream()
					.filter(i -> i.getPayload().getPayloadType() == F950
							&& "PROCESSED".equals(i.getPayload().getState())
							&& "950".equals(i.eventCode)).findFirst();
			if (CAR_STATE_SUBMITTED.equals(carNew.getState())
					&& carNew.getDealer() != null
					&& submitPayloadItem.isPresent()) {
				carNew.setState(CAR_STATE_READY_TO_FINANCE);
				submitPayloadItem.get().eventCode = "READY";
			} else if (carNew.getDealer() == null
					&& CAR_STATE_SUBMITTED.equals(carNew.getState())) {
				carNew.setState(CAR_STATE_CANCEL);
			}

			return carNew;
		}

	}

	public static class ReadyToFinanceState implements StateProccess {
		private static final Log log = LogFactory
				.getLog(ReadyToSubmitState.class);

		@Override
		public Car proccess(Car carOld, Car carNew) {
			log.info("ReadyToFinanceState process started");

			Optional<PayloadItem> finRequestPayloadItem = carNew
					.getPayloadItems()
					.stream()
					.filter(p -> p.getPayload().getPayloadType() == F120
							&& "PROCESSED".equals(p.payload.state)).findFirst();

			if (CAR_STATE_READY_TO_FINANCE.equals(carNew.getState())
					&& carNew.getDealer() != null
					&& finRequestPayloadItem.isPresent()) {
				carNew.setState(CAR_STATE_FINANCE_REQUESTED);
			} else if (carNew.getDealer() == null
					&& CAR_STATE_READY_TO_FINANCE.equals(carNew.getState())) {
				carNew.setState(CAR_STATE_CANCEL);
			}

			return carNew;
		}

	}

	public static class FinancedState implements StateProccess {
		private static final Log log = LogFactory.getLog(FinancedState.class);

		@Override
		public Car proccess(Car carOld, Car carNew) {
			log.info("FinancedState process started");

			Optional<PayloadItem> fullRepayedPayloadItem = carNew
					.getPayloadItems()
					.stream()
					.filter(p -> p.getPayload().getPayloadType() == F920
							&& "PROCESSED".equals(p.payload.state)).findFirst();

			if (CAR_STATE_FINANCED.equals(carNew.getState())
					&& carNew.getDealer() != null
					&& fullRepayedPayloadItem.isPresent()) {
				carNew.setState(CAR_STATE_REPAID);
			}

			return carNew;
		}

	}

	public static class FinanceRequestedState implements StateProccess {
		private static final Log log = LogFactory
				.getLog(FinanceRequestedState.class);

		@Override
		public Car proccess(Car carOld, Car carNew) {
			log.info("FinanceRequestedState process started");

			Optional<PayloadItem> item = carNew
					.getPayloadItems()
					.stream()
					.filter(p -> (p.getPayload().getPayloadType() == F910
							&& ("RECIEVED".equals(p.payload.state) || "PROCESSED"
									.equals(p.payload.state))) 
							|| (p.getPayload().getPayloadType() == F120
									&& "ROLLBACK".equals(p.payload.state))
									).findFirst();

			if (CAR_STATE_FINANCE_REQUESTED.equals(carNew.getState())
					&& carNew.getDealer() != null && item.isPresent()
					&& item.get().getPayload().getPayloadType() == F910
					) {
				carNew.setState(CAR_STATE_FINANCED);
			} else if (CAR_STATE_FINANCE_REQUESTED.equals(carNew.getState())
					&& carNew.getDealer() != null && item.isPresent()
					&& item.get().getPayload().getPayloadType() == F120) {
				carNew.setState(CAR_STATE_READY_TO_FINANCE);
			}

			return carNew;
		}

	}

	public static class FullRepayedState implements StateProccess {
		private static final Log log = LogFactory.getLog(FinancedState.class);

		@Override
		public Car proccess(Car carOld, Car carNew) {
			log.info("FullRepayedState process started");

			if (CAR_STATE_REPAID.equals(carNew.getState())
					&& carNew.getArchivedDate() != null) {
				carNew.setState(CAR_STATE_ARCHIVED);
			}

			return carNew;
		}

	}
	
	public static class ICflRequestedState implements StateProccess {
		private static final Log log = LogFactory
				.getLog(ICflRequestedState.class);

		@Override
		public Car proccess(Car carOld, Car carNew) {
			log.info("ICflRequestedState process started");

			Optional<PayloadItem> item = carNew
					.getPayloadItems()
					.stream()
					.filter(p -> p.getPayload().getPayloadType() == ICFL
							&& ("RECIEVED".equals(p.payload.state) || "PROCESSED"
									.equals(p.payload.state))).findFirst();

			if (item.isPresent()) {
				carNew.setState(CAR_STATE_NEW);
			}

			return carNew;
		}

	}

	public static CarStateProcessor build(CarRepository carRepository, Car car) {
		log.info(String.format("CarStateProcessor build started for vin = %s",
				car.getVin()));

		CarStateProcessor carStateProcessor = new CarStateProcessor();
		carStateProcessor.carNew = car;

		if (car.getId() != 0) {
			Optional<Car> carOld = carRepository.findById(car.getId());
			carOld.ifPresent(c -> carStateProcessor.carOld = c);
		}

		if (CAR_STATE_NEW.equals(car.getState()))
			carStateProcessor.stateProccess = new NewState();
		else if (CAR_STATE_READY_TO_SUBMIT.equals(car.getState()))
			carStateProcessor.stateProccess = new ReadyToSubmitState();
		else if (CAR_STATE_SUBMITTED_CFL.equals(car.getState()))
			carStateProcessor.stateProccess = new SubmittedCflState();
		else if (CAR_STATE_SUBMITTED.equals(car.getState()))
			carStateProcessor.stateProccess = new SubmittedState();
		else if (CAR_STATE_READY_TO_FINANCE.equals(car.getState()))
			carStateProcessor.stateProccess = new ReadyToFinanceState();
		else if (CAR_STATE_FINANCE_REQUESTED.equals(car.getState()))
			carStateProcessor.stateProccess = new FinanceRequestedState();
		else if (CAR_STATE_FINANCED.equals(car.getState()))
			carStateProcessor.stateProccess = new FinancedState();
		else if (CAR_STATE_REPAID.equals(car.state))
			carStateProcessor.stateProccess = new FullRepayedState();
		else if (CAR_STATE_CANCEL.equals(car.getState()))
			carStateProcessor.stateProccess = new CanceledState();
		else if (CAR_STATE_ICFL.equals(car.getState()))
			carStateProcessor.stateProccess = new ICflRequestedState();
		else
			carStateProcessor.stateProccess = new DefaultState();

		log.info("CarStateProcessor build finished");

		return carStateProcessor;
	}

}
