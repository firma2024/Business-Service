package com.firma.business.exception;

import lombok.Getter;

@Getter
public class ErrorIntegrationServiceException extends Exception{
    int statusCode;
    public ErrorIntegrationServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
