package com.tikal.atm.errors.exceptions;

public class MaximumWithdrawalException extends Exception {
    public MaximumWithdrawalException(String errorMessage) {
        super(errorMessage);
    }
}