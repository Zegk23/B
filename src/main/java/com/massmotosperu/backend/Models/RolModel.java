package com.massmotosperu.backend.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tb_rol")  
@NoArgsConstructor
@AllArgsConstructor
public class RolModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID autoincremental
    @Column(name = "IDRol", updatable = false, nullable = false)
    private int idRol;

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Column(name = "NombreRol", nullable = false)
    private String nombreRol;
}
