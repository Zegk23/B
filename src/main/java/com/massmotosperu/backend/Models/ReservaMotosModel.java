package com.massmotosperu.backend.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "tb_reservas")
@NoArgsConstructor
@AllArgsConstructor
public class ReservaMotosModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDReserva", updatable = false, nullable = false)
    private int idReserva;

    @NotNull(message = "La fecha de la reserva es obligatoria")
    @FutureOrPresent(message = "La fecha de reserva debe ser hoy o en el futuro")
    @Column(name = "Fecha", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @NotNull(message = "La moto asociada a la reserva es obligatoria")
    @ManyToOne
    @JoinColumn(name = "IDMoto", referencedColumnName = "IDMoto", nullable = false)
    private MotoModel moto;

    @NotNull(message = "La tienda asociada a la reserva es obligatoria")
    @ManyToOne
    @JoinColumn(name = "IDTienda", referencedColumnName = "IDTienda", nullable = false)
    private SedeTiendaModel tienda;

    @NotNull(message = "El usuario asociado a la reserva es obligatorio")
    @ManyToOne
    @JoinColumn(name = "IDUsuario", referencedColumnName = "IDUsuario", nullable = false)
    private UsuarioModel usuario;

}
