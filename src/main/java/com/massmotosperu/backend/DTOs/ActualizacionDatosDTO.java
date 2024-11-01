package com.massmotosperu.backend.DTOs;

import lombok.Data;

@Data
public class ActualizacionDatosDTO {
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correoElectronico;
    private String telefono;
    private int edad;
    private String contraseña;
    private String dni;
    private String preNombre;
}
