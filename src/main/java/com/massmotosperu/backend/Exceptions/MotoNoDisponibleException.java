package com.massmotosperu.backend.Exceptions;

public class MotoNoDisponibleException extends RuntimeException {
    public MotoNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
