package com.massmotosperu.backend.Exceptions;

public class TiendaNoEncontradaException extends RuntimeException {
    public TiendaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
