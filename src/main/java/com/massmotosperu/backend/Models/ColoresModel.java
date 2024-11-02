package com.massmotosperu.backend.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tb_colores")  
@NoArgsConstructor
@AllArgsConstructor
public class ColoresModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    @Column(name = "IDColor", updatable = false, nullable = false) 
    private int idColor;

    @NotBlank(message = "El nombre del color no puede estar en blanco") 
    @Size(max = 50, message = "El nombre del color no debe exceder los 50 caracteres") 
    @Column(name = "NombreColor", nullable = false)
    private String nombreColor;

    @ManyToOne
    @JoinColumn(name = "IDMoto", referencedColumnName = "IDMoto")
    private MotoModel moto;
}
