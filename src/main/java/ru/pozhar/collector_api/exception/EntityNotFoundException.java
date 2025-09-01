package ru.pozhar.collector_api.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String massage) {
        super(massage);
    }
}
