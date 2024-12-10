package com.massmotosperu.backend.Exceptions;

public class ErrorEnvioCorreoException extends RuntimeException {
    public ErrorEnvioCorreoException(String mensaje) {
        super(mensaje);
    }
}
