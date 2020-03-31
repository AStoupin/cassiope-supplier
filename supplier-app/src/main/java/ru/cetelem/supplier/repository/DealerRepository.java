package ru.cetelem.supplier.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.cetelem.cassiope.supplier.model.Car;
import ru.cetelem.cassiope.supplier.model.Dealer;


@Repository
public interface DealerRepository extends CrudRepository<Dealer, Integer> {
	@Query("select d from Dealer d where d.code = :code")
	public Optional<Dealer> findByCode(@Param("code") String code);
}
