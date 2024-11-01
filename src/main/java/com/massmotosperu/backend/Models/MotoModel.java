package com.massmotosperu.backend.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tb_motos")  
@NoArgsConstructor
@AllArgsConstructor
public class MotoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    @Column(name = "IDMoto", updatable = false, nullable = false)
    private int idMoto;

    @NotBlank(message = "El nombre de la moto es obligatorio")
    @Size(max = 100, message = "El nombre de la moto no debe exceder los 100 caracteres")
    @Column(name = "NombreMoto", nullable = false)
    private String nombreMoto;

    @Size(max = 50, message = "El estado de la moto no debe exceder los 50 caracteres")
    @Column(name = "EstadoMoto")
    private String estadoMoto;

    @NotBlank(message = "La marca de la moto es obligatoria")
    @Column(name = "MarcaMoto", nullable = false)
    private String marcaMoto;

    @NotBlank(message = "El modelo de la moto es obligatorio")
    @Column(name = "ModeloMoto", nullable = false)
    private String modeloMoto;

    @Min(value = 1900, message = "El año de fabricación debe ser un valor válido")
    @Column(name = "AnioFabricacion")
    private int anioFabricacion;

    @Positive(message = "La velocidad máxima debe ser un valor positivo")
    @Column(name = "VelocidadMaxima")
    private double velocidadMaxima;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio en soles debe ser mayor a 0")
    @Column(name = "PrecioSoles")
    private double precioSoles;

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio en dólares debe ser mayor a 0")
    @Column(name = "PrecioDolares")
    private double precioDolares;

    @Min(value = 0, message = "El kilometraje debe ser cero o mayor")
    @Column(name = "Kilometraje")
    private int kilometraje;

    @NotNull(message = "La disponibilidad es obligatoria")
    @Column(name = "Disponibilidad")
    private String disponibilidad;

    @Positive(message = "El peso debe ser un valor positivo")
    @Column(name = "Peso")
    private double peso;

    @Positive(message = "El número de asientos debe ser un valor positivo")
    @Column(name = "NumAsientos")
    private int numAsientos;

    @Positive(message = "La carga útil debe ser un valor positivo")
    @Column(name = "CargaUtil")
    private int cargaUtil;

    @Positive(message = "La garantía en años debe ser un valor positivo")
    @Column(name = "GarantiaAnios")
    private int garantiaAnios;

    @Positive(message = "La garantía en kilómetros debe ser un valor positivo")
    @Column(name = "GarantiaKM")
    private int garantiaKM;

    @Size(max = 5000, message = "La URL de la imagen no debe exceder los 255 caracteres")
    @Column(name = "URLImagen")
    private String urlImagen;

    @Size(max = 1000, message = "La descripción de la moto no debe exceder los 1000 caracteres")
    @Column(name = "DescripcionMoto", length = 1000)
    private String descripcionMoto;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "IDMotor", referencedColumnName = "IDMotor")
    private MotorModel motor;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "IDSuspension", referencedColumnName = "IDSuspension")
    private SuspensionesModel suspensiones;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "IDCombustible", referencedColumnName = "IDCombustible")
    private CombustiblesModel combustibles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "IDNeumatico", referencedColumnName = "IDNeumatico")
    private NeumaticosModel neumaticos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "IDFreno", referencedColumnName = "IDFreno")
    private FrenosModel frenos;
}
