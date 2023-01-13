package com.tikal.atm;

import com.force.api.ApiError;
import com.tikal.atm.services.ATMService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value="atm")
public class ATMController {

    private final ATMService atmService;

    @PostMapping("/withdrawal")
    @ApiOperation(value = "",  notes = "Searches inventory for itineraries between two airports, which may include connecting flight(s)")
    public ResponseEntity<Object> withdrawal(@RequestParam(name = "amount") float amount) {
        try {
            return ResponseEntity.ok(atmService.withdrawal(amount));
        } catch (Exception e) {
            ApiError apiError = new ApiError();
            apiError.setMessage(e.getLocalizedMessage());
            apiError.setApiErrorCode("400");
            return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/refill")
    @ApiOperation(value = "",  notes = "Searches inventory for itineraries between two airports, which may include connecting flight(s)")
    public ResponseEntity<Object> refill(@RequestBody JSONObject input) {
        try {
            return ResponseEntity.ok(atmService.refill(input));
        } catch (Exception e) {
            ApiError apiError = new ApiError();
            apiError.setMessage(e.getLocalizedMessage());
            apiError.setApiErrorCode("400");
            return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
    }
}
