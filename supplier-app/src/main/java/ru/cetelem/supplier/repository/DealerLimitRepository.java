package ru.cetelem.supplier.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.cetelem.cassiope.supplier.model.Dealer;
import ru.cetelem.cassiope.supplier.model.DealerLimit;

@Repository
public interface DealerLimitRepository extends CrudRepository<DealerLimit, Integer> {
    @Query("select d from DealerLimit d where d.dealer = :dealer")
    public Optional<DealerLimit> findByDealer(@Param("dealer") Dealer dealer);
}
