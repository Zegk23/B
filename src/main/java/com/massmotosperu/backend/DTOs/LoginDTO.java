package com.massmotosperu.backend.DTOs;

import lombok.Data;

@Data
public class LoginDTO {
    private String correoElectronico;
    private String contraseña;
}
