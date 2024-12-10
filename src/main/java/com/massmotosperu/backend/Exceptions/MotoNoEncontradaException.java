package com.massmotosperu.backend.Exceptions;

public class MotoNoEncontradaException extends RuntimeException {
    public MotoNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
