package ru.cetelem.supplier.service;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.cetelem.cassiope.supplier.io.PayloadType;
import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.supplier.repository.PayloadRepository;

public class PayloadStateProcessor {
	private static final Log log = LogFactory.getLog(PayloadStateProcessor.class); 
	
	private StateProccess stateProccess;
	private Payload payloadNew;
	private Payload payloadOld;
	
	public Payload proccess() {
		return stateProccess.proccess(payloadOld, payloadNew);
	}
	
	interface StateProccess{
		default Payload proccess(Payload payloadOld, Payload payloadNew)  {
			return payloadNew;
		}
	}

	public static class DefaultState implements StateProccess {
	}
	
	public static class NewState implements StateProccess {
		private static final Log log = LogFactory.getLog(NewState.class); 

		@Override
		public Payload proccess(Payload payloadOld, Payload payloadNew) {
			log.info("NewState process started");
			
			
			if("NEW".equals(payloadNew.state) 
					&& payloadNew.date != null ) {
				payloadNew.state = payloadNew.getPayloadType().direction == PayloadType.Direction.OUT ? "SUBMITTED" : "RECIEVED";
			}
				
			return payloadNew;	
		}
		
	}

	public static class SubmittedState implements StateProccess {
		private static final Log log = LogFactory.getLog(SubmittedState.class); 

		@Override
		public Payload proccess(Payload payloadOld, Payload payloadNew) {
			log.info("SubmittedState process started");
			
			
			if("SUBMITTED".equals(payloadNew.state) 
					&& payloadNew.processedDate != null ) {
				payloadNew.state = "PROCESSED";
			}
				
			return payloadNew;	
		}
		
	}
	
	
	public static class RecievedState implements StateProccess {
		private static final Log log = LogFactory.getLog(SubmittedState.class); 

		@Override
		public Payload proccess(Payload payloadOld, Payload payloadNew) {
			log.info("RecievedState process started");
			
			
			if("RECIEVED".equals(payloadNew.state) 
					&& payloadNew.processedDate != null ) {
				long errorCount = payloadNew.getPayloadItems().stream().filter(pi->!"OK".equals(pi.errorDescr)).count();
				
				payloadNew.state = errorCount==0 ? "PROCESSED" : "FAILED";
			}
				
			return payloadNew;	
		}
		
	}

	public static class ProcessedState implements StateProccess {
		private static final Log log = LogFactory.getLog(SubmittedState.class); 

		@Override
		public Payload proccess(Payload payloadOld, Payload payloadNew) {
			log.info("ProcessedState process started");
			
			
			if("PROCESSED".equals(payloadNew.state) 
					&& payloadNew.rollbackDate != null ) {
				payloadNew.state = "ROLLBACK";
			} 
				
			return payloadNew;	
		}
		
	}
		
	
	public static PayloadStateProcessor build(PayloadRepository payloadRepository, Payload payload) {
		log.info(String.format("PayloadStateProcessor build started for name = %s", payload.name ));

		PayloadStateProcessor payloadStateProcessor = new PayloadStateProcessor();
		payloadStateProcessor.payloadNew = payload;
		
		
		if(payload.id != 0) {
			Optional<Payload> payloadOld = payloadRepository.findByName(payload.name);
			payloadOld.ifPresent(p->payloadStateProcessor.payloadOld = p);
			 
		}
		
		if("NEW".equals(payload.state)) 
			payloadStateProcessor.stateProccess = new NewState();

		else if("SUBMITTED".equals(payload.state)) 
			payloadStateProcessor.stateProccess = new SubmittedState();
		
		else if("RECIEVED".equals(payload.state)) 
			payloadStateProcessor.stateProccess = new RecievedState();
		
		else if("PROCESSED".equals(payload.state)) 
			payloadStateProcessor.stateProccess = new ProcessedState();

		else
			payloadStateProcessor.stateProccess = new DefaultState();
		

		log.info("PayloadStateProcessor build finished");
		
		return payloadStateProcessor;
	}

}
