package ru.cetelem.supplier.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.cetelem.cassiope.supplier.model.CarModel;
import ru.cetelem.cassiope.supplier.model.Payload;
import ru.cetelem.cassiope.supplier.model.PayloadItem;

@Repository
public interface PayloadItemRepository  extends CrudRepository<PayloadItem, Integer>{
	
	@Query("select i from PayloadItem i where i.payload = :payload")
	public List<PayloadItem> findByPayload(@Param("payload") Payload payload);	
}
