// ReservaMotosRepository.java
package com.massmotosperu.backend.Repositories;

import com.massmotosperu.backend.Models.MotoModel;
import com.massmotosperu.backend.Models.ReservaMotosModel;
import com.massmotosperu.backend.Models.SedeTiendaModel;
import com.massmotosperu.backend.Models.UsuarioModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaMotosRepository extends JpaRepository<ReservaMotosModel, Integer> {
    List<ReservaMotosModel> findByMoto(MotoModel moto);
    boolean existsByMotoAndTienda(MotoModel moto, SedeTiendaModel tienda);
    
    List<ReservaMotosModel> findByUsuario(UsuarioModel usuario);
}
