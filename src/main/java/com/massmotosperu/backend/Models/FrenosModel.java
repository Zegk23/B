package com.massmotosperu.backend.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tb_frenos") 
@NoArgsConstructor
@AllArgsConstructor
public class FrenosModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    @Column(name = "IDFreno", updatable = false, nullable = false)
    private int idFreno;
    
    @NotBlank(message = "El tipo de freno no puede estar vacío")
    @Column(name = "TipoFreno", nullable = false)
    private String tipoFreno;

    @DecimalMin(value = "0.0", inclusive = false, message = "El freno delantero debe ser mayor a 0")
    @Column(name = "FrenadoDelantero", nullable = false)
    private double frenadoDelantero;

    @DecimalMin(value = "0.0", inclusive = false, message = "El freno trasero debe ser mayor a 0")
    @Column(name = "FrenadoTrasero", nullable = false)
    private double frenadoTrasero;
}
