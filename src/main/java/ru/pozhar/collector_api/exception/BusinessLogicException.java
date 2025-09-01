package ru.pozhar.collector_api.exception;

public class BusinessLogicException extends RuntimeException {
    public BusinessLogicException(String massage) {
        super(massage);
    }
}
