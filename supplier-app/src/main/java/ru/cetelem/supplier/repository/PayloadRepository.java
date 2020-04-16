package ru.cetelem.supplier.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.cetelem.cassiope.supplier.io.PayloadType;
import ru.cetelem.cassiope.supplier.model.Payload;

@Repository
public interface PayloadRepository  extends CrudRepository<Payload, Integer> {

	@Query("select p from Payload p order by p.date desc")
	public Iterable<Payload> findAll();

	@Query("select p from Payload p where p.id = (select max(r.id) from Payload r where r.name = :name)")
	public Optional<Payload> findByNameOne(@Param("name") String name);

	@Query("select p from Payload p where p.name = :name")
	public Optional<Payload> findByName(@Param("name") String name);

	@Query("select p from Payload p where p.payloadType = :payloadType and p.state = :state ")
	public List<Payload> getPayloadByStatus(@Param("payloadType") PayloadType payloadType, @Param("state") String status);

	@Query("select p from Payload p where p.state = :state ")
	public List<Payload> getPayloadByStatus(@Param("state") String status);
	
	@Query("select c from Payload c where c.archivedDate is null")
	public List<Payload> findAllWithoutArchive();
	

	@Query("select max(p.sequenceNumber) from Payload p where p.payloadType = :payloadType ")
	public int getPayloadMaxSequence(@Param("payloadType")  PayloadType payloadType);

}
