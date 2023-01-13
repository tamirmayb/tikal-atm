package com.tikal.atm.errors.exceptions;

public class MaximumCoinsWithdrawalException extends Exception {
    public MaximumCoinsWithdrawalException(String errorMessage) {
        super(errorMessage);
    }
}