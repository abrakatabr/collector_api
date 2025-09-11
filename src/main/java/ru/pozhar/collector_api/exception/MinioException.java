package ru.pozhar.collector_api.exception;

public class MinioException extends RuntimeException{
    public MinioException(String massage) { super(massage); }
}
