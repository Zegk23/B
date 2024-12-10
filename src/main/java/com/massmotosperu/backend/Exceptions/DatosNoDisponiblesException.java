package com.massmotosperu.backend.Exceptions;

public class DatosNoDisponiblesException extends RuntimeException {
    public DatosNoDisponiblesException(String mensaje) {
        super(mensaje);
    }
}
