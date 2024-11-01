package com.massmotosperu.backend.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tb_neumaticos")  
@NoArgsConstructor
@AllArgsConstructor
public class NeumaticosModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID autoincremental
    @Column(name = "IDNeumatico", updatable = false, nullable = false)
    private int idNeumatico;

    @NotBlank(message = "La descripción del neumático delantero es obligatoria")
    @Column(name = "NeumaticoDelantero", nullable = false)
    private String neumaticoDelantero;

    @NotBlank(message = "La descripción del neumático trasero es obligatoria")
    @Column(name = "NeumaticoTrasero", nullable = false)
    private String neumaticoTrasero;

    @Min(value = 1, message = "El aro delantero debe ser mayor o igual a 1")
    @Column(name = "AnchoDelantero", nullable = false)
    private int anchoDelantero;

    @Min(value = 1, message = "El aro trasero debe ser mayor o igual a 1")
    @Column(name = "AnchoTrasero", nullable = false)
    private int anchoTrasero;
}
