package com.tikal.atm.services;

import com.tikal.atm.dto.ATMItemDTO;
import com.tikal.atm.model.ATMItem;
import com.tikal.atm.model.Money;
import com.tikal.atm.repositories.ATMRepository;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ATMService {
    private final ATMRepository atmRepository;

    public void initATM(List<Money> allMoney) {
        List<ATMItem> items = new ArrayList<>();
        allMoney.forEach(a-> items.add(ATMItem.of(a.getMoneyId(), 0)));

        atmRepository.deleteAll();
        atmRepository.saveAll(items);
    }

    public Object withdrawal(float amount) {
        List<ATMItem> allMoney = atmRepository.findAll();
            Map<String, ATMItem> map = new TreeMap<>(allMoney.stream()
                    .collect(Collectors.toMap(ATMItem::getMoneyId, Function.identity()))).descendingMap();

            return calcBillsAndCoins(amount, map);
    }

    private List<ATMItemDTO> calcBillsAndCoins(float withdraw, Map<String, ATMItem> map) {
        List<ATMItemDTO> result = new ArrayList<>();

        for(Map.Entry<String, ATMItem> entry : map.entrySet()) {
            float value = Float.parseFloat(entry.getKey());
            int countOfMoneyItems = (int)Math.floor(withdraw / value);
            if(countOfMoneyItems > 0) {
                if(countOfMoneyItems > entry.getValue().getAmount()) {
                    countOfMoneyItems = entry.getValue().getAmount();
                }
                result.add(ATMItemDTO.of(entry.getKey(), countOfMoneyItems));
                entry.getValue().setAmount(entry.getValue().getAmount() - countOfMoneyItems);
                withdraw = withdraw - (value * countOfMoneyItems);
                if(withdraw == 0) {
                    break;
                }
            }
        }
        return result;
    }

    public Object refill(JSONObject input) {
        Map<String, Integer> map = (Map<String, Integer>) input.get("money");
        map.forEach((id, addAmount) -> {
            Optional<ATMItem> byId = atmRepository.findById(id);
            if(byId.isPresent()) {
                ATMItem atmItem = byId.get();
                atmItem.setAmount(atmItem.getAmount() + addAmount);
                atmRepository.save(atmItem);
            }
        });
        return null;
    }
}
