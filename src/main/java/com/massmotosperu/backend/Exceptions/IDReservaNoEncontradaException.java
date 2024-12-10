package com.massmotosperu.backend.Exceptions;

public class IDReservaNoEncontradaException extends RuntimeException {
    public IDReservaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
