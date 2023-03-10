package com.tikal.atm;

import com.tikal.atm.dto.ATMWithdrawalResultWrapperDTO;
import com.tikal.atm.dto.RefillResultDTO;
import com.tikal.atm.services.ATMService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "atm")
public class ATMController {

    private final ATMService atmService;

    @PostMapping("/withdrawal")
    @ApiOperation(value = "", notes = "")
    public ResponseEntity<ATMWithdrawalResultWrapperDTO> withdrawal(@RequestBody() JSONObject input) {
        return ResponseEntity.ok(atmService.withdrawal(input));
    }

    @PostMapping("/refill")
    @ApiOperation(value = "", notes = "")
    public ResponseEntity<List<RefillResultDTO>> refill(@RequestBody JSONObject input) {
        return ResponseEntity.ok(atmService.refill(input));
    }
}
