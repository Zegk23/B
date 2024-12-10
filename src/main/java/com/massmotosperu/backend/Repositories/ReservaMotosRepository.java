// ReservaMotosRepository.java
package com.massmotosperu.backend.Repositories;

import com.massmotosperu.backend.Models.MotoModel;
import com.massmotosperu.backend.Models.ReservaMotosModel;
import com.massmotosperu.backend.Models.SedeTiendaModel;
import com.massmotosperu.backend.Models.UsuarioModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaMotosRepository extends JpaRepository<ReservaMotosModel, Integer> {

    boolean existsByMotoAndTiendaAndEstado(MotoModel moto, SedeTiendaModel tienda, String estado);

    List<ReservaMotosModel> findByUsuario(UsuarioModel usuario);

    List<ReservaMotosModel> findByMotoAndEstado(MotoModel moto, String estado);

    List<ReservaMotosModel> findByMotoAndTiendaAndEstado(MotoModel moto, SedeTiendaModel tienda, String estado);


    @Query("SELECT m.marcaMoto, COUNT(r) FROM ReservaMotosModel r " +
            "JOIN r.moto m " +
            "WHERE r.estado = 'PENDIENTE' " +
            "GROUP BY m.marcaMoto " +
            "ORDER BY COUNT(r) DESC")
    List<Object[]> countReservasPorMarca();

     @Query("SELECT COUNT(r) FROM ReservaMotosModel r WHERE r.estado = :estado")
    long countByEstado(@Param("estado") String estado);
}
