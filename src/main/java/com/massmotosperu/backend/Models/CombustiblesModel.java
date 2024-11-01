package com.massmotosperu.backend.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tb_combustibles")  
@NoArgsConstructor
@AllArgsConstructor
public class CombustiblesModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "IDCombustible", updatable = false, nullable = false)
    private int idCombustible;

    @NotBlank(message = "El tipo de combustible no puede estar vacío")
    @Column(name = "TipoCombustible", nullable = false)
    private String tipoCombustible;

    @NotBlank(message = "El sistema de combustible no puede estar vacío")
    @Column(name = "SistemaCombustible", nullable = false)
    private String sistemaCombustible;

    @DecimalMin(value = "0.0", inclusive = false, message = "La capacidad del tanque debe ser mayor a 0")
    @Column(name = "CapacidadDeposito", nullable = false)
    private double CapacidadDeposito;

    @DecimalMin(value = "0.0", inclusive = false, message = "La autonomía debe ser mayor a 0")
    @Column(name = "Autonomia", nullable = false)
    private double autonomia;

    @DecimalMin(value = "0.0", inclusive = false, message = "El rendimiento por galón debe ser mayor a 0")
    @Column(name = "RendimientoPorLitro", nullable = false)
    private double rendimientoPorLitro;
}
