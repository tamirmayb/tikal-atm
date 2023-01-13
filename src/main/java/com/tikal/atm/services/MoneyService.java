package com.tikal.atm.services;

import com.tikal.atm.model.Money;
import com.tikal.atm.model.Type;
import com.tikal.atm.repositories.MoneyRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MoneyService implements IMoneyService{
    private static final String COINS_PROPS = "#{'${money.coins.values}'.split(',')}";
    private static final String BILLS_PROPS = "#{'${money.bills.values}'.split(',')}";

    private final MoneyRepository moneyRepository;

    private final ATMService atmService;

    @Value(COINS_PROPS)
    private List<String> coins;

    @Value(BILLS_PROPS)
    private List<String> bills;

    public void initMoney() {
        List<Money> allMoney = new ArrayList<>();
        coins.forEach(c-> allMoney.add(Money.of(c, Type.COIN)));
        bills.forEach(b-> allMoney.add(Money.of(b, Type.BILL)));

        moneyRepository.deleteAll();
        moneyRepository.saveAll(allMoney);

        atmService.initATM(allMoney);
    }
}
