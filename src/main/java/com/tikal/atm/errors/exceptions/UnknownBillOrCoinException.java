package com.tikal.atm.errors.exceptions;

public class UnknownBillOrCoinException extends RuntimeException {
    public UnknownBillOrCoinException(String errorMessage) {
        super(errorMessage);
    }
}