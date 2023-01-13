package com.tikal.atm.services;

import com.tikal.atm.dto.ATMWithdrawalResultWrapperDTO;
import com.tikal.atm.model.Money;
import org.json.simple.JSONObject;

import java.util.List;

public interface IATMService {
    void initATM(List<Money> allMoney);
    String refill(JSONObject input);
    ATMWithdrawalResultWrapperDTO withdrawal(JSONObject input);

}
