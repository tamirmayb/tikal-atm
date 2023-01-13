package com.tikal.atm.errors;

import com.tikal.atm.errors.exceptions.MaximumCoinsWithdrawalException;
import com.tikal.atm.errors.exceptions.MaximumWithdrawalException;
import com.tikal.atm.errors.exceptions.NotEnoughMoneyException;
import com.tikal.atm.errors.exceptions.UnknownBillOrCoinException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(UnknownBillOrCoinException.class)
    protected ResponseEntity<Object> unknownBillOrCoin(
            UnknownBillOrCoinException ex) {
        ApiError apiError = new ApiError(UNPROCESSABLE_ENTITY); // 422
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(MaximumWithdrawalException.class)
    protected ResponseEntity<Object> maximumWithdrawalReached(
            MaximumWithdrawalException ex) {
        ApiError apiError = new ApiError(UNPROCESSABLE_ENTITY); // 422
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(MaximumCoinsWithdrawalException.class)
    protected ResponseEntity<Object> maximumCoinsWithdrawalReached(
            MaximumCoinsWithdrawalException ex) {
        ApiError apiError = new ApiError(UNPROCESSABLE_ENTITY); // 422
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    protected ResponseEntity<Object> NotEnoughMoney(
            NotEnoughMoneyException ex) {
        ApiError apiError = new ApiError(CONFLICT); // 409
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

}