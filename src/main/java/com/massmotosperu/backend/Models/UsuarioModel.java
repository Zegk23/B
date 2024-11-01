package com.massmotosperu.backend.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tb_usuario")  // Nombre de la tabla en minúsculas para coincidir con la base de datos
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID autoincremental
    @Column(name = "IDUsuario", updatable = false, nullable = false)
    private int idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "Nombre", nullable = false)
    private String nombre;

    @Column(name = "PreNombre")
    private String preNombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Column(name = "ApellidoPaterno", nullable = false)
    private String apellidoPaterno;

    @Column(name = "ApellidoMaterno")
    private String apellidoMaterno;

    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 8, message = "El DNI debe tener 8 caracteres")
    @Pattern(regexp = "\\d+", message = "El DNI solo debe contener números")
    @Column(name = "Dni", nullable = false, length = 8)
    private String dni;

    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "El teléfono debe contener entre 7 y 15 dígitos")
    @Column(name = "Telefono")
    private String telefono;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Debe proporcionar un correo electrónico válido")
    @Column(name = "CorreoElectronico", unique = true, nullable = false)
    private String correoElectronico;

    @Positive(message = "La edad debe ser un número positivo")
    @Column(name = "Edad")
    private int edad;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(name = "Contraseña", nullable = false)
    private String contraseña;
}
