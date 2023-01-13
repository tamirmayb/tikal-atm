package com.tikal.atm.services;

import com.google.gson.JsonObject;
import com.tikal.atm.dto.ATMWithdrawalResultWrapperDTO;
import com.tikal.atm.dto.RefillResultDTO;
import com.tikal.atm.model.ATMItem;
import com.tikal.atm.model.Money;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;

public interface IATMService {
    void initATM(List<Money> allMoney);
    List<RefillResultDTO> refill(JSONObject input);
    ATMWithdrawalResultWrapperDTO withdrawal(JSONObject input);
    void saveATM(Map<Float, ATMItem> map);

}
