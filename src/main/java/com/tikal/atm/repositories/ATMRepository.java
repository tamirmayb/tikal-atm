package com.tikal.atm.repositories;

import com.tikal.atm.model.ATMItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ATMRepository extends MongoRepository<ATMItem, String> {
    Optional<List<ATMItem>> findByAmountGreaterThan(int amount);
    Optional<ATMItem> findByMoneyMoneyId(String amount);
}

