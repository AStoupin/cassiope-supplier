package ru.cetelem.supplier.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.cetelem.cassiope.supplier.model.Car;

@Repository
public interface CarRepository extends PagingAndSortingRepository<Car, Integer> {
	@Query("select c from Car c where c.vin = :vin")
	public Optional<Car> findByVin(@Param("vin") String vin);
	
	@Query("select c from Car c where c.state = :state")
	public List<Car> findByState(@Param("state") String state);

	@Query("select c from Car c where c.archivedDate is null")
	public List<Car> findAllWithoutArchive();
}
