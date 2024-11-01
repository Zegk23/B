package com.massmotosperu.backend.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tb_motores")  
@NoArgsConstructor
@AllArgsConstructor
public class MotorModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID autoincremental
    @Column(name = "IDMotor", updatable = false, nullable = false)
    private int idMotor;

    @NotBlank(message = "El tipo de motor es obligatorio")
    @Column(name = "TipoMotor", nullable = false)
    private String tipoMotor;

    @Positive(message = "La cilindrada debe ser un valor positivo")
    @Column(name = "Cilindrada", nullable = false)
    private int cilindrada;

    @Min(value = 1, message = "El número de cilindros debe ser al menos 1")
    @Column(name = "NumeroCilindros", nullable = false)
    private int numeroCilindros;
    
    @NotBlank(message = "El sistema de enfriamiento es obligatorio")
    @Column(name = "SistemaDeEnfriamiento", nullable = false)
    private String sistemaDeEnfriamiento;
    
    @NotBlank(message = "El sistema de encendido es obligatorio")
    @Column(name = "SistemaDeEncendido", nullable = false)
    private String sistemaDeEncendido;

    @DecimalMin(value = "0.0", inclusive = false, message = "La potencia debe ser mayor a 0")
    @Column(name = "Potencia", nullable = false)
    private double potencia;

    @DecimalMin(value = "0.0", inclusive = false, message = "El torque debe ser mayor a 0")
    @Column(name = "Torque", nullable = false)
    private double torque;
}
