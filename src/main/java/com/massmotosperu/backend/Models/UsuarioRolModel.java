package com.massmotosperu.backend.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tb_usuario_roles") 
@IdClass(UsuarioRolId.class)
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRolModel {

    @Id
    @NotNull(message = "El ID del usuario no puede ser nulo")
    @Min(value = 1, message = "El ID del usuario debe ser mayor o igual a 1")
    @Column(name = "IDUsuario")
    private int usuarioID;

    @Id
    @NotNull(message = "El ID del rol no puede ser nulo")
    @Min(value = 1, message = "El ID del rol debe ser mayor o igual a 1")
    @Column(name = "IDRol")
    private int rolID;

    @ManyToOne
    @JoinColumn(name = "IDUsuario", referencedColumnName = "IDUsuario", insertable = false, updatable = false)
    private UsuarioModel usuario;

    @ManyToOne
    @JoinColumn(name = "IDRol", referencedColumnName = "IDRol", insertable = false, updatable = false)
    private RolModel rol;
}
