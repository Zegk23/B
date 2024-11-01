package com.massmotosperu.backend.Repositories;

import com.massmotosperu.backend.Models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {
    Optional<UsuarioModel> findByCorreoElectronico(String correoElectronico);
}
