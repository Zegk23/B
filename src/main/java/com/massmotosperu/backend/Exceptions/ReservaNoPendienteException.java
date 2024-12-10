package com.massmotosperu.backend.Exceptions;

public class ReservaNoPendienteException extends RuntimeException {
    public ReservaNoPendienteException(String mensaje) {
        super(mensaje);
    }
}
