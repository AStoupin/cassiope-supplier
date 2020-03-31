package ru.cetelem.supplier.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.cetelem.cassiope.supplier.model.FinancePlan;


@Repository
public interface FinancePlanRepository extends CrudRepository<FinancePlan, Integer> {
	@Query("select f from FinancePlan f where f.code = :code")
	public Optional<FinancePlan> findByCode(@Param("code") String code);
}
