package com.massmotosperu.backend.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tb_sedetienda")
@NoArgsConstructor
@AllArgsConstructor
public class SedeTiendaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID autoincremental
    @Column(name = "IDTienda", updatable = false, nullable = false)
    private int idTienda;

    @NotBlank(message = "El nombre de la tienda es obligatorio")
    @Size(max = 100, message = "El nombre de la tienda no debe exceder los 100 caracteres")
    @Column(name = "Nombre", nullable = false, length = 100)
    private String nombreTienda;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "El teléfono debe contener entre 7 y 15 dígitos")
    @Size(min = 7, max = 15, message = "El teléfono debe tener entre 7 y 15 caracteres")
    @Column(name = "TelefonoTienda", length = 15)
    private String telefonoTienda;

    @NotBlank(message = "La ubicación es obligatoria")
    @Size(max = 100, message = "La ubicación no debe exceder los 100 caracteres")
    @Column(name = "Ubicacion", nullable = false, length = 100)
    private String ubicacion;
}
