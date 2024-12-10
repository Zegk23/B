package com.massmotosperu.backend.Exceptions;

public class MotoNoEncontradaEnElLocalException extends RuntimeException {
    public MotoNoEncontradaEnElLocalException(String mensaje) {
        super(mensaje);
    }
}
