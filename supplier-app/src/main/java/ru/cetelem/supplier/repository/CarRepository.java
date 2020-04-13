package ru.cetelem.supplier.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
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


	
	@Query("select c from Car c where c.archivedDate is not null and (:vin = '' or c.vin like '%' || :vin  || '%' ) "
			+ "and (:dateFrom is null or :dateFrom <= c.fullRepaymentDate) and (:dateTo is null or :dateTo >= c.fullRepaymentDate)"
			+ "and (:dealer = '' or c.dealer is not null and upper(c.dealer.name) like '%' || upper(:dealer)  || '%' )"
			+ " and rownum < 100")
	public List<Car> findInArchive(@Param("vin") String vin, @Param("dateFrom") LocalDate dateFrom, 
			@Param("dateTo")  LocalDate dateTo, @Param("dealer")  String dealer );

}
