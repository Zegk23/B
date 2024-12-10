package com.massmotosperu.backend.Exceptions;

public class EdadNoValidaException extends RuntimeException {
    public EdadNoValidaException(String mensaje) {
        super(mensaje);
    }
}
