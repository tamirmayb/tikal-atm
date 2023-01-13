import com.fasterxml.jackson.core.JsonProcessingException;
import com.tikal.atm.dto.ATMWithdrawalResultWrapperDTO;
import com.tikal.atm.dto.RefillResultDTO;
import com.tikal.atm.errors.exceptions.NotEnoughMoneyException;
import com.tikal.atm.errors.exceptions.UnknownBillOrCoinException;
import com.tikal.atm.model.ATMItem;
import com.tikal.atm.model.Money;
import com.tikal.atm.model.Type;
import com.tikal.atm.repositories.ATMRepository;
import com.tikal.atm.services.ATMService;
import com.tikal.atm.services.IATMService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import org.springframework.core.env.Environment;

import java.util.*;

import static com.tikal.atm.services.ATMService.MAX_COINS_PARAM;
import static com.tikal.atm.services.ATMService.MAX_WITHDRAWAL_PARAM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ATMTest {
    IATMService atmService;

    private ATMRepository atmRepository;
    private Environment env;

    private final ATMItem item001 = ATMItem.of(Money.of("0.01", Type.COIN), 10L);
    private final ATMItem item01 = ATMItem.of(Money.of("0.1", Type.COIN), 1L);
    private final ATMItem item1 = ATMItem.of(Money.of("1", Type.COIN), 10L);
    private final ATMItem item5 = ATMItem.of(Money.of("5", Type.COIN), 10L);
    private final ATMItem item10 = ATMItem.of(Money.of("10", Type.COIN), 10L);
    private final ATMItem item20 = ATMItem.of(Money.of("20", Type.BILL), 5L);
    private final ATMItem item100 = ATMItem.of(Money.of("100", Type.BILL), 2L);
    private final ATMItem item200 = ATMItem.of(Money.of("200", Type.BILL), 1L);
    private final List<ATMItem> allMoney = Arrays.asList(item001, item01, item1, item5, item10, item20, item100, item200);

    @Before
    public void init() {
        env = mock(Environment.class);
        atmRepository = mock(ATMRepository.class);
        atmService = new ATMService(atmRepository, env);
    }

    @Test()
    @Description("refill happy path")
    public void refill() {
        when(this.atmRepository.findByMoneyMoneyId("0.01"))
                .thenAnswer(ans-> Optional.of(item001));
        when(this.atmRepository.findByMoneyMoneyId("0.1"))
                .thenAnswer(ans-> Optional.of(item01));
        when(this.atmRepository.findByMoneyMoneyId("1"))
                .thenAnswer(ans-> Optional.of(item1));
        when(this.atmRepository.findByMoneyMoneyId("5"))
                .thenAnswer(ans-> Optional.of(item5));
        when(this.atmRepository.findByMoneyMoneyId("10"))
                .thenAnswer(ans-> Optional.of(item10));
        when(this.atmRepository.findByMoneyMoneyId("20"))
                .thenAnswer(ans-> Optional.of(item20));
        when(this.atmRepository.findByMoneyMoneyId("100"))
                .thenAnswer(ans-> Optional.of(item100));
        when(this.atmRepository.findByMoneyMoneyId("200"))
                .thenAnswer(ans-> Optional.of(item200));

        List<RefillResultDTO> expected = new ArrayList<>();
        expected.add(RefillResultDTO.of("100", 32L));
        expected.add(RefillResultDTO.of("0.1", 6L));
        expected.add(RefillResultDTO.of("5", 30L));
        expected.add(RefillResultDTO.of("20", 20L));

        List<RefillResultDTO> results = atmService.refill(getInputJsonForRefill());
        assertEquals(4, results.size());
        assertEquals(results, expected);
    }

    @Test(expected = UnknownBillOrCoinException.class)
    @Description("refill money not found")
    public void refillMoneyNotFound() {
        when(this.atmRepository.findByMoneyMoneyId("0.1"))
                .thenAnswer(ans-> Optional.of(item01));

        atmService.refill(getInputJsonForRefill());
    }

    @Test()
    @Description("withdrawal happy path")
    public void withdrawal() throws ParseException {

        when(this.atmRepository.findByMoneyMoneyId("0.01"))
                .thenAnswer(ans-> Optional.of(item001));
        when(this.atmRepository.findByMoneyMoneyId("0.1"))
                .thenAnswer(ans-> Optional.of(item01));
        when(this.atmRepository.findByMoneyMoneyId("1"))
                .thenAnswer(ans-> Optional.of(item1));
        when(this.atmRepository.findByMoneyMoneyId("5"))
                .thenAnswer(ans-> Optional.of(item5));
        when(this.atmRepository.findByMoneyMoneyId("10"))
                .thenAnswer(ans-> Optional.of(item10));
        when(this.atmRepository.findByMoneyMoneyId("20"))
                .thenAnswer(ans-> Optional.of(item20));
        when(this.atmRepository.findByMoneyMoneyId("100"))
                .thenAnswer(ans-> Optional.of(item100));
        when(this.atmRepository.findByMoneyMoneyId("200"))
                .thenAnswer(ans-> Optional.of(item200));

        when(this.atmRepository.findByAmountGreaterThan(0L))
                .thenAnswer(ans-> Optional.of(allMoney));

        when(this.env.getProperty(MAX_WITHDRAWAL_PARAM))
                .thenAnswer(ans-> "2000");
        when(this.env.getProperty(MAX_COINS_PARAM))
                .thenAnswer(ans-> "50");

        String jsonStrForInput = "{\n" +
                "\"amount\": 660.12,\n" +
                "}";

        JSONParser parser = new JSONParser();
        JSONObject input = (JSONObject) parser.parse(jsonStrForInput);

        ATMWithdrawalResultWrapperDTO withdrawalResult = atmService.withdrawal(input);

        assertNotNull(withdrawalResult);
        assertEquals(withdrawalResult.getBills().size(), 3);
        assertEquals(withdrawalResult.getCoins().size(), 5);
    }

    @Test(expected = NotEnoughMoneyException.class)
    @Description("withdrawal not enough money")
    public void withdrawal_not_enough_money() throws ParseException {
        when(this.atmRepository.findByMoneyMoneyId("0.01"))
                .thenAnswer(ans-> Optional.of(item001));
        when(this.atmRepository.findByMoneyMoneyId("0.1"))
                .thenAnswer(ans-> Optional.of(item01));
        when(this.atmRepository.findByMoneyMoneyId("1"))
                .thenAnswer(ans-> Optional.of(item1));
        when(this.atmRepository.findByMoneyMoneyId("5"))
                .thenAnswer(ans-> Optional.of(item5));
        when(this.atmRepository.findByMoneyMoneyId("10"))
                .thenAnswer(ans-> Optional.of(item10));
        when(this.atmRepository.findByMoneyMoneyId("20"))
                .thenAnswer(ans-> Optional.of(item20));
        when(this.atmRepository.findByMoneyMoneyId("100"))
                .thenAnswer(ans-> Optional.of(item100));
        when(this.atmRepository.findByMoneyMoneyId("200"))
                .thenAnswer(ans-> Optional.of(item200));

        when(this.atmRepository.findByAmountGreaterThan(0L))
                .thenAnswer(ans-> Optional.of(allMoney));

        when(this.env.getProperty(MAX_WITHDRAWAL_PARAM))
                .thenAnswer(ans-> "2000");
        when(this.env.getProperty(MAX_COINS_PARAM))
                .thenAnswer(ans-> "50");

        String jsonStrForInput = "{\n" +
                "\"amount\": 960.10,\n" +
                "}";

        JSONParser parser = new JSONParser();
        JSONObject input = (JSONObject) parser.parse(jsonStrForInput);

        atmService.withdrawal(input);
    }

    private JSONObject getInputJsonForRefill() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("0.1", 5);
        inputMap.put("5", 20);
        inputMap.put("20", 15);
        inputMap.put("100", 30);

        JSONObject input = new JSONObject();
        input.put("money", inputMap);
        return input;
    }

}
