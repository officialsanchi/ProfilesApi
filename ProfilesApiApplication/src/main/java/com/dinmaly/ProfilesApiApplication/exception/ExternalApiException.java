package com.dinmaly.ProfilesApiApplication.exception;

public class ExternalApiException extends RuntimeException {
    public ExternalApiException(String apiName) {

        super(apiName + " returned an invalid response");
    }
}
