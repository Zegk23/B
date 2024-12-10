package com.massmotosperu.backend.Exceptions;

public class OperacionDuplicadaException extends RuntimeException {
    public OperacionDuplicadaException(String mensaje) {
        super(mensaje);
    }
}
