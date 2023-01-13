package com.tikal.atm.repositories;

import com.tikal.atm.model.Money;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyRepository extends MongoRepository<Money, String> {

}

