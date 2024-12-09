package com.massmotosperu.backend.Repositories;

import com.massmotosperu.backend.Models.SedeTiendaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SedeTiendaRepository extends JpaRepository<SedeTiendaModel, Integer> {

    // Encuentra tiendas donde está disponible una moto específica
    @Query("SELECT t FROM SedeTiendaModel t WHERE t.idTienda IN " +
            "(SELECT d.idTienda FROM DisponibilidadMotoModel d WHERE d.idMoto = :idMoto)")
    List<SedeTiendaModel> findTiendasByMotoId(@Param("idMoto") Integer idMoto);

    // Contar el total de tiendas activas (que tienen al menos una moto disponible o
    // reservada)
    @Query("SELECT COUNT(DISTINCT t) " +
            "FROM SedeTiendaModel t " +
            "JOIN DisponibilidadMotoModel d ON t.idTienda = d.idTienda")
    long countActiveStores();

    @Query("""
                SELECT t.nombreTienda,
                       COUNT(DISTINCT CASE WHEN m.disponibilidad = 'Disponible' THEN m.idMoto ELSE NULL END) AS disponibles,
                       COUNT(DISTINCT CASE WHEN r.estado = 'PENDIENTE' THEN r.idReserva ELSE NULL END) AS reservadas
                FROM SedeTiendaModel t
                LEFT JOIN DisponibilidadMotoModel d ON t.idTienda = d.idTienda
                LEFT JOIN MotoModel m ON d.idMoto = m.idMoto
                LEFT JOIN ReservaMotosModel r ON r.moto.idMoto = m.idMoto AND r.tienda.idTienda = t.idTienda AND r.estado = 'PENDIENTE'
                GROUP BY t.nombreTienda
            """)
    List<Object[]> motosDisponiblesVsReservadasPorTienda();

    @Query("""
                SELECT m.marcaMoto AS marca, COUNT(r.idReserva) AS totalReservas
                FROM ReservaMotosModel r
                JOIN MotoModel m ON r.moto.idMoto = m.idMoto
                WHERE r.estado = 'PENDIENTE'
                GROUP BY m.marcaMoto
            """)
    List<Object[]> countReservasPorMarca();

}
