package com.massmotosperu.backend.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler({
        UsuarioYaExisteException.class,
        TelefonoYaRegistradoException.class,
        EdadNoValidaException.class,
        IDReservaNoEncontradaException.class,
        MotoYaReservadaException.class,
        MotoNoDisponibleException.class,
        MotoNoEncontradaEnElLocalException.class,
        TiendaNoEncontradaException.class,
        ReservaNoPendienteException.class,
        OperacionDuplicadaException.class,
        RecursoAsociadoException.class,
        AccionNoPermitidaException.class
    })
    public ResponseEntity<?> handleCustomExceptions(RuntimeException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
