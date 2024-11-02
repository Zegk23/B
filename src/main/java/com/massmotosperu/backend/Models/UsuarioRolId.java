package com.massmotosperu.backend.Models;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UsuarioRolId implements Serializable {

    @NotNull(message = "El ID del usuario no puede ser nulo")
    @Min(value = 1, message = "El ID del usuario debe ser mayor o igual a 1")
    private int usuarioID;

    @NotNull(message = "El ID del rol no puede ser nulo")
    @Min(value = 1, message = "El ID del rol debe ser mayor o igual a 1")
    private int rolID;

    public UsuarioRolId() {}

    public UsuarioRolId(int usuarioID, int rolID) {
        this.usuarioID = usuarioID;
        this.rolID = rolID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioRolId that = (UsuarioRolId) o;
        return rolID == that.rolID && usuarioID == that.usuarioID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioID, rolID);
    }

    public int getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(int usuarioID) {
        this.usuarioID = usuarioID;
    }

    public int getRolID() {
        return rolID;
    }

    public void setRolID(int rolID) {
        this.rolID = rolID;
    }
}
