package com.massmotosperu.backend.Models;

import java.io.Serializable;
import java.util.Objects;

public class UsuarioRolId implements Serializable {

    private int usuarioID;  
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
