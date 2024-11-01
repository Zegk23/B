package com.massmotosperu.backend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tb_usuariorol")  
@IdClass(UsuarioRolId.class)  
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRolModel {

    @Id
    @Column(name = "IDUsuario")  
    private int usuarioID;

    @Id
    @Column(name = "IDRol")
    private int rolID;

    @ManyToOne
    @JoinColumn(name = "IDUsuario", referencedColumnName = "IDUsuario", insertable = false, updatable = false)
    private UsuarioModel usuario;

    @ManyToOne
    @JoinColumn(name = "IDRol", referencedColumnName = "IDRol", insertable = false, updatable = false)
    private RolModel rol;
}
