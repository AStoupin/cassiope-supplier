package ru.cetelem.supplier.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.cetelem.cassiope.supplier.io.PayloadType;
import ru.cetelem.cassiope.supplier.io.cfl.Cfl11Item;
import ru.cetelem.cassiope.supplier.io.cfl.Cfl22Item;
import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.cassiope.supplier.model.PayloadItem;
import ru.cetelem.cassiope.supplier.util.DateUtils;
import ru.cetelem.supplier.integration.AbstractPayloadItemProcessor;
import ru.cetelem.supplier.integration.PayloadItemProcessorFactory;
import ru.cetelem.supplier.repository.PayloadItemRepository;
import ru.cetelem.supplier.repository.PayloadRepository;
import ru.cetelem.supplier.util.Configurator;

@Service
@Transactional
public class PayloadService {
	private static final Log log = LogFactory.getLog(PayloadService.class); 
	
	private PayloadRepository payloadRepository;
	@SuppressWarnings("unused")
	private PayloadItemRepository payloadItemRepository;
	private PayloadItemProcessorFactory payloadItemProcessorFactory;
	
	private CarService carService;

	@Autowired
	private Environment environment;
	
	public PayloadService(
	    @Autowired PayloadRepository payloadRepository,
	    @Autowired PayloadItemRepository payloadItemRepository,
	    @Autowired CarService carService //
	) {
		log.info("PayloadService create started ");

		this.payloadRepository = payloadRepository;
		this.payloadItemRepository = payloadItemRepository;
		this.carService = carService;

		this.payloadItemProcessorFactory = new PayloadItemProcessorFactory(carService, this);


		log.info("PayloadService create finished ");
	}

	public List<Payload> getPayloads(){
		log.info("getPayloads started");
		
		Iterable<Payload> iterable =  payloadRepository.findAll();
		List<Payload> payloads = StreamSupport.stream(iterable.spliterator(), false) .collect(Collectors.toList()); 

		log.info("getPayloads finished");
		
		return payloads;
	}
	
	
	public List<Payload> getCarsWithoutArchive() {
		log.info("CarService getCarsWithoutArchive started");
		
		List<Payload> payloads =  payloadRepository.findAllWithoutArchive();
		
		//List<Car> models = StreamSupport.stream(iterable.spliterator(), false) .collect(Collectors.toList()); 

		log.info("CarService getCarsWithoutArchive finished");
		return payloads;

	}	
	public Optional<Payload> getPayloadByName(String name) {
		log.info("getPayloadByName started"); 
		return payloadRepository.findByName(name);
	}
	
	public Optional<Payload> getPayloadByNameOne(String name) {
		log.info("getPayloadByNameOne started"); 
		return payloadRepository.findByNameOne(name);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Payload savePayload(Payload payload) {
		log.info("savePayload started"); 
		
		payload = PayloadStateProcessor.build(payloadRepository, payload).proccess();
		
 		payloadRepository.save(payload);
		
		if(!"FAILED".contains(payload.state)) {

			payload.getPayloadItems().stream().filter(pi->pi.getCar()!=null).map(pi->pi.getCar())
				.forEach(carService::saveCar);

			payload.getPayloadItems().stream().filter(pi->pi.getLimit()!=null).map(pi->pi.getDealer())
				.forEach(carService.getDealerService()::saveDealer);

		}
		
		return payload;
	}
	
	public String generatePayloadName(PayloadType payloadType) {
		log.info(String.format("generatePayloadName started for %s", payloadType));
		
		if(payloadType==null)
			return "";

		String prefixFile = null;
		if (PayloadType.CFL.equals(payloadType)) {
			prefixFile = environment.getProperty(Configurator.MASK_CFL);
		} else if (PayloadType.F120.equals(payloadType)) {
			prefixFile = environment.getProperty(Configurator.MASK_F120);
		} else if (PayloadType.F150.equals(payloadType)) {
			prefixFile = environment.getProperty(Configurator.MASK_F150);
		} else {
			prefixFile = payloadType.toString();
		}

		return prefixFile + LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")) +
			    LocalTime.now().format(DateTimeFormatter.ofPattern("-HHmmss"));
	}	

	@Deprecated
	public void generateOutputPayloadItemsFor(Payload payload) {
		log.info(String.format("generateOutputPayloadItemsFor executed for payload %s", payload.name));
		
		if(payload.payloadItems.size()>0)
			throw new RuntimeException("Payload already initialized");
		
		payloadItemProcessorFactory.build(payload, environment).process();

		log.info("generateOutputPayloadItemsFor finished");			

	}

	public AbstractPayloadItemProcessor getPayloadItemProcessor(Payload payload) {
		log.info(String.format("getPayloadItemProcessor executed for payload %s", payload.name));
		
		AbstractPayloadItemProcessor result;
		
		result = payloadItemProcessorFactory.build(payload, environment);
		
		log.info("getPayloadItemProcessor finished");			
		
		return result;

	}
	
	public void proccessInputPayloadItemsFor(Payload payload) {
		log.info(String.format("proccessInputPayloadItemsFor executed for payload %s", payload.name));		
		
		payloadItemProcessorFactory.build(payload, environment).process();

		payload.processedDate = LocalDate.now();
		savePayload(payload);
		
		log.info("proccessInputPayloadItemsFor finished");					
	}
		
	public PayloadItem createPayloadItemByObject(Payload payload, Object object) {
		if (payload == null)
			log.info("createPayloadItemForObject - payload == null");
		else
			log.info(String.format("createPayloadItemForObject for %s", payload.name));
		
		ObjectMapper jsonMapper  = new ObjectMapper();
		
		PayloadItem item =  new PayloadItem(payload);
		try {
			item.source  = jsonMapper.writeValueAsString(object);
			item.sourceType = object.getClass().getCanonicalName();
			if (object instanceof Cfl22Item) {
				Cfl22Item cfl22Item = (Cfl22Item)object;
				item.car = cfl22Item.car;
			} else if (object instanceof Cfl11Item) {
				Cfl11Item cfl11Item = (Cfl11Item)object;
				item.car = cfl11Item.car;
			}
		} catch (JsonProcessingException e) {
			log.error(e);
			throw new RuntimeException(e);
		}

		payload.payloadItems.add(item);
		
		return item;
	}
		
	public Object createObjectByPayloadItem(PayloadItem payloadItem) {
		log.info("createObjectByPayloadItem started");
		
		ObjectMapper jsonMapper  = new ObjectMapper();
		jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		Object o = null;
		try {
			 o = jsonMapper.readValue(payloadItem.source, Class.forName(payloadItem.sourceType));
		} catch (ClassNotFoundException | IOException e) {
			log.info(payloadItem.sourceType + " | " + payloadItem.source);
			log.error(e);
			throw new RuntimeException(e);
		}
		
		return o;
	}
	
	public void submitPayload(Payload payload) {
		log.info(String.format("submitPayload started for %s", payload.name));
		
		if(!"NEW".equals(payload.state))
			throw new RuntimeException("Incorrect state. To be NEW state to submit a payload");

		payload.date = payload.date==null ? LocalDateTime.now():payload.date;  
		
		savePayload(payload);
		
		log.info("submitPayload finished");
	}

	public List<Payload> getPayloadByStatus(PayloadType payloadType, String status) {
		return payloadRepository.getPayloadByStatus(payloadType, status);
	}
	
	public List<Payload> getPayloadByStatus( String status) {
		return payloadRepository.getPayloadByStatus(status);
	}

	public int generatePayloadSequence(PayloadType payloadType) {
		int seq = 0;
		if(payloadType!=null)
			try {
				seq = payloadRepository.getPayloadMaxSequence(payloadType) + 1;
			} catch(org.springframework.aop.AopInvocationException e) {
				log.info("generatePayloadSequence org.springframework.aop.AopInvocationException: getPayloadMaxSequence not found for " + payloadType);
			}
		return seq;
	}
	
	public Environment getEnvironment() {
		return environment;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int archivePayloads(Set<Payload> payloads, LocalDate archiveDate, int days) {
		int count = 0;
		for(Payload payload : payloads) {
			if(payload.getArchivedDate() == null) {
				long daysBetween = ChronoUnit.DAYS.between(
						payload.getDate(), 
						DateUtils.asLocalDateTime(archiveDate));
				if(daysBetween >= days) {
					payload.setArchivedDate(archiveDate);
					payloadRepository.save(payload);
					count++;
				}
			}
		}
		return count;
	}
	
}
