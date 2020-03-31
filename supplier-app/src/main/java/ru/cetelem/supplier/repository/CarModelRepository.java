package ru.cetelem.supplier.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.cetelem.cassiope.supplier.model.CarModel;
import ru.cetelem.cassiope.supplier.model.Dealer;

import java.util.Optional;

@Repository
public interface CarModelRepository extends CrudRepository<CarModel, Integer> {
    @Query("select cm from CarModel cm where cm.code = :code")
    public Optional<CarModel> findByCode(@Param("code") String code);
}
