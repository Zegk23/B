package com.massmotosperu.backend.DTOs;

import lombok.Data;

@Data
public class ReservaDTO {
    private int idMoto;
    private String idTienda;
    private int idUsuario;
    private String fechaReserva;
}
