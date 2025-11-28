package com.zjgsu.lll.course2_new.exception;

public class InvalidEnrollmentStatusException extends RuntimeException {
    public InvalidEnrollmentStatusException(String message) {
        super(message);
    }
}