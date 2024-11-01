package com.massmotosperu.backend.Repositories;

import com.massmotosperu.backend.Models.UsuarioRolModel;
import com.massmotosperu.backend.Models.UsuarioRolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRolModel, UsuarioRolId> {
}
