package com.tikal.atm.services;

import com.tikal.atm.dto.ATMItemDTO;
import com.tikal.atm.model.ATMItem;
import com.tikal.atm.model.Money;
import com.tikal.atm.repositories.ATMRepository;
import com.tikal.atm.utils.Utils;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.slf4j.helpers.Util;
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
        allMoney.forEach(a-> items.add(ATMItem.of(a, 0)));

        atmRepository.deleteAll();
        atmRepository.saveAll(items);
    }

    public Object withdrawal(float amount) {
        Optional<List<ATMItem>> allMoney = atmRepository.findByAmountGreaterThan(0);
        if(allMoney.isPresent()) {
            Map<Float, ATMItem> map = new TreeMap<>(allMoney.get().stream()
                    .collect(Collectors.toMap(ATMItem::getMoneyValue, Function.identity()))).descendingMap();
            return calcBillsAndCoins(amount, map);
        }
        return new ArrayList<>();
    }

    private List<ATMItemDTO> calcBillsAndCoins(float withdrawParam, Map<Float, ATMItem> map) {
        List<ATMItemDTO> result = new ArrayList<>();
        float withdraw = Utils.roundFloat(withdrawParam);

        for(Map.Entry<Float, ATMItem> entry : map.entrySet()) {
            float value = entry.getKey();
            int countOfMoneyItems = (int)Math.floor(withdraw / value);
            if(countOfMoneyItems > 0) {
                if(countOfMoneyItems > entry.getValue().getAmount()) {
                    countOfMoneyItems = entry.getValue().getAmount();
                }
                result.add(ATMItemDTO.of(entry.getKey().toString(), countOfMoneyItems));
                entry.getValue().setAmount(entry.getValue().getAmount() - countOfMoneyItems);
                withdraw = Utils.roundFloat(withdraw - (value * countOfMoneyItems));
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
            Optional<ATMItem> byId = atmRepository.findByMoneyMoneyId(id);
            if(byId.isPresent()) {
                ATMItem atmItem = byId.get();
                atmItem.setAmount(atmItem.getAmount() + addAmount);
                atmRepository.save(atmItem);
            }
        });
        return null;
    }
}
