package com.firma.business.exception;

import lombok.Getter;

@Getter
public class ErrorDataServiceException extends Exception{
    private int statusCode;
    public ErrorDataServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
