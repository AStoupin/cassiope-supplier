package ru.cetelem.supplier.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.cetelem.cassiope.supplier.model.Dealer;
import ru.cetelem.cassiope.supplier.model.DealerLimit;
import ru.cetelem.supplier.repository.DealerLimitRepository;

@Service
public class DealerLimitService {
	private static final Log log = LogFactory.getLog(DealerLimitService.class);	
	
	public DealerLimitRepository dealerLimitRepository;

	@SuppressWarnings("unused")
	private List<DealerLimit> dealerLimits;
	
	@Autowired 
	DealerLimitService(DealerLimitRepository dealerLimitRepository){
		this.dealerLimitRepository = dealerLimitRepository;
	}

	public List<DealerLimit> getdealerLimits(){
		log.info("getdealerLimits started");
		
		Iterable<DealerLimit> iterable =  dealerLimitRepository.findAll();
		List<DealerLimit> limits = StreamSupport.stream(iterable.spliterator(), false) .collect(Collectors.toList()); 

		log.info("getdealerLimits finished");
		return limits;
	}
	
	public Optional<DealerLimit> findByDealer(Dealer dealer){
		return dealerLimitRepository.findByDealer(dealer);
	}

	public void saveDealerLimit(DealerLimit dealerLimit) {
		dealerLimitRepository.save(dealerLimit);
	}	

}
