package com.massmotosperu.backend.Models;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tb_disponibilidad_moto")
public class DisponibilidadMotoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDDisponibilidad")
    private Integer idDisponibilidad;

    @NotNull(message = "El ID de la moto no puede ser nulo")
    @Column(name = "IDMoto")
    private Integer idMoto;

    @NotNull(message = "El ID de la tienda no puede ser nulo")
    @Column(name = "IDTienda")
    private Integer idTienda;
}
