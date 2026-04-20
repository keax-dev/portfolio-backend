package com.keax.shared.domain.exceptions;

public class ExceptionAlert extends RuntimeException{

    public ExceptionAlert(String alert) {
        super(alert);
    }

}
