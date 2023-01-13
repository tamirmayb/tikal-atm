package com.tikal.atm.services;

import com.tikal.atm.Application;
import com.tikal.atm.dto.ATMItemDTO;
import com.tikal.atm.dto.ATMWithdrawalResultDTO;
import com.tikal.atm.dto.ATMWithdrawalResultWrapperDTO;
import com.tikal.atm.dto.RefillResultDTO;
import com.tikal.atm.errors.exceptions.MaximumCoinsWithdrawalException;
import com.tikal.atm.errors.exceptions.MaximumWithdrawalException;
import com.tikal.atm.errors.exceptions.NotEnoughMoneyException;
import com.tikal.atm.errors.exceptions.UnknownBillOrCoinException;
import com.tikal.atm.model.ATMItem;
import com.tikal.atm.model.Money;
import com.tikal.atm.model.Type;
import com.tikal.atm.repositories.ATMRepository;
import com.tikal.atm.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ATMService implements IATMService{
    public static final String MAX_WITHDRAWAL_PARAM = "atm.max.withdrawal";
    public static final String MAX_COINS_PARAM = "atm.max.coins";

    private static final Logger log = LogManager.getLogger(Application.class);

    private final ATMRepository atmRepository;

    private final Environment env;

    public void initATM(List<Money> allMoney) {
        List<ATMItem> items = new ArrayList<>();
        allMoney.forEach(a-> items.add(ATMItem.of(a, 0L)));

        atmRepository.deleteAll();
        atmRepository.saveAll(items);
    }

    @SneakyThrows
    public List<RefillResultDTO> refill(JSONObject input) {
        Map<String, Integer> map = (Map<String, Integer>) input.get("money");
        List<RefillResultDTO> results = new ArrayList<>();
        map.forEach((id, addAmount) -> {
            Optional<ATMItem> byId = atmRepository.findByMoneyMoneyId(id);
            if(byId.isPresent()) {
                ATMItem atmItem = byId.get();
                atmItem.setAmount(atmItem.getAmount() + addAmount);
                atmRepository.save(atmItem);

                results.add(RefillResultDTO.of(id, atmItem.getAmount()));
            } else {
                throw new UnknownBillOrCoinException("Bill or coin does not exist and cannot be added " + id);
            }
        });
        return results;
    }

    @SneakyThrows
    public ATMWithdrawalResultWrapperDTO withdrawal(JSONObject input) {
        double amountInput = (double) input.get("amount");
        float amount = (float) amountInput;
        String maxWithdrawal = env.getProperty(MAX_WITHDRAWAL_PARAM);
        assert maxWithdrawal != null;
        if(amount > Float.parseFloat(maxWithdrawal)) {
            throw new MaximumWithdrawalException("Maximum Withdrawal amount is " + maxWithdrawal);
        }

        List<ATMItemDTO> result = new ArrayList<>();
        Optional<List<ATMItem>> allMoney = atmRepository.findByAmountGreaterThan(0L);
        if(allMoney.isPresent()) {
            Map<Float, ATMItem> map = new TreeMap<>(allMoney.get().stream()
                    .collect(Collectors.toMap(ATMItem::getMoneyValue, Function.identity()))).descendingMap();
            result.addAll(dispenseBillsAndCoins(amount, map));
        }
        return processResult(result);
    }

    @SneakyThrows
    private List<ATMItemDTO> dispenseBillsAndCoins(float withdrawParam, Map<Float, ATMItem> map) {
        log.info("in calcBillsAndCoins, trying to dispense amount " + withdrawParam);

        List<ATMItemDTO> result = new ArrayList<>();
        String maxCoinsWithdrawal = env.getProperty(MAX_COINS_PARAM);

        int coinCount = 0;
        float available = 0;
        float withdraw = Utils.roundFloat(withdrawParam);

        for(Map.Entry<Float, ATMItem> entry : map.entrySet()) {
            float value = entry.getKey();
            Long atmItemsToBeDispensedCount = (long) (int) Math.floor(withdraw / value);
            if(atmItemsToBeDispensedCount > 0) {
                if(atmItemsToBeDispensedCount > entry.getValue().getAmount()) {
                    atmItemsToBeDispensedCount = entry.getValue().getAmount();
                }

                result.add(ATMItemDTO.of(entry.getKey().toString(), entry.getValue().getMoney().getType(), atmItemsToBeDispensedCount));
                entry.getValue().setAmount(entry.getValue().getAmount() - atmItemsToBeDispensedCount);

                if(entry.getValue().getMoney().getType().equals(Type.COIN)) {
                    coinCount += atmItemsToBeDispensedCount;
                    assert maxCoinsWithdrawal != null;
                    if(coinCount > Integer.parseInt(maxCoinsWithdrawal)) {
                        log.error("in calcBillsAndCoins, too many coins");
                        throw new MaximumCoinsWithdrawalException("too many coins");
                    }
                }
                float dispensed = value * atmItemsToBeDispensedCount;
                available += dispensed;
                withdraw = Utils.roundFloat(withdraw - dispensed);
                if(withdraw == 0) {
                    break;
                }
            }
        }

        if(withdraw > 0) {
            log.error("in calcBillsAndCoins, not enough money to withdraw");
            throw new NotEnoughMoneyException("There's not enough money to withdraw, this ATM can only dispense " + Utils.roundFloat(available));
        }
        saveATM(map);
        return result;
    }

    public void saveATM(Map<Float, ATMItem> map) {
        map.values().forEach(atmRepository::save);
    }

    private ATMWithdrawalResultWrapperDTO processResult(List<ATMItemDTO> result) {
        List<ATMWithdrawalResultDTO> bills = new ArrayList<>();
        List<ATMWithdrawalResultDTO> coins = new ArrayList<>();
        result.forEach(r-> {
            switch (r.getType()) {
                case BILL:
                    bills.add(ATMWithdrawalResultDTO.fromDTO(r));
                    return;
                case COIN:
                    coins.add(ATMWithdrawalResultDTO.fromDTO(r));
                    return;
                default:
                    log.error("found invalid money type " + r.getType());
            }
        });
        return ATMWithdrawalResultWrapperDTO.of(bills, coins);
    }
}
