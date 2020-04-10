package ru.cetelem.supplier.service;

import java.math.BigDecimal;
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
import ru.cetelem.supplier.repository.DealerRepository;

@Service
public class DealerService {
	private static final Log log = LogFactory.getLog(DealerService.class);

	public DealerRepository dealerRepository;

	@SuppressWarnings("unused")
	private List<Dealer> dealers;
	
	@Autowired 
	DealerService(DealerRepository dealerRepository){
		this.dealerRepository = dealerRepository;
	}

	public List<Dealer> getDealers(){
		log.info("getDealers started");
		
		Iterable<Dealer> iterable =  dealerRepository.findAll();
		List<Dealer> models = StreamSupport.stream(iterable.spliterator(), false) .collect(Collectors.toList()); 

		log.info("getDealers finished");
		return models;
	}
	
	public Optional<Dealer> findByCode(String dealerCode){
		return dealerRepository.findByCode(dealerCode);
	}

	public void saveDealer(Dealer dealer) {
		dealerRepository.save(dealer);
	}	

	public void updateDealerLimit(DealerLimit dealerLimit, double hardLimit, double softLimit, double totalFinanced,
			double availableAmount, double sublimitDeCc, double totalFinancedDeCc, double availableAmountDeCc) {

		dealerLimit.setHardLimit(new BigDecimal(hardLimit));
		dealerLimit.setSoftLimit(new BigDecimal(softLimit));
		dealerLimit.setTotalFinanced(new BigDecimal(totalFinanced));

		dealerLimit.setAvailableAmount(new BigDecimal(availableAmount));
		dealerLimit.setSublimitDeCc(new BigDecimal(sublimitDeCc));
		dealerLimit.setTotalFinancedDeCc(new BigDecimal(totalFinancedDeCc));
		dealerLimit.setAvailableAmountDeCc(new BigDecimal(availableAmountDeCc));
	}

}
