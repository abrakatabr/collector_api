package ru.pozhar.collector_api.exception;

public class ValidationException extends RuntimeException{
    public ValidationException(String massage) {
        super(massage);
    }
}
