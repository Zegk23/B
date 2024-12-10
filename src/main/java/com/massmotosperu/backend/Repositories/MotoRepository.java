package com.massmotosperu.backend.Repositories;

import com.massmotosperu.backend.Models.MotoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MotoRepository extends JpaRepository<MotoModel, Integer> {
    Optional<MotoModel> findByNombreMoto(String nombre);

    long countByDisponibilidad(String disponibilidad);

    @Query("SELECT AVG(m.precioSoles) FROM MotoModel m WHERE m.disponibilidad = :disponibilidad")
    Double findAveragePriceByDisponibilidad(@Param("disponibilidad") String disponibilidad);

    @Query("SELECT m.marcaMoto, COUNT(r) " +
            "FROM ReservaMotosModel r " +
            "JOIN r.moto m " +
            "WHERE r.estado = 'PENDIENTE' " +
            "GROUP BY m.marcaMoto " +
            "ORDER BY COUNT(r) DESC")
    List<Object[]> countReservasPorMarca();

    @Query("SELECT DISTINCT m.marcaMoto FROM MotoModel m")
    List<String> findAllDistinctMarcas();

    @Query("SELECT COUNT(m) FROM MotoModel m WHERE m.estadoMoto = :estado")
    long countByEstado(@Param("estado") String estado);
    
}
