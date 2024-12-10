package com.massmotosperu.backend.Repositories;

import com.massmotosperu.backend.Models.DisponibilidadMotoModel;
import com.massmotosperu.backend.Models.MotoModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisponibilidadMotoRepository extends JpaRepository<DisponibilidadMotoModel, Integer> {

    List<DisponibilidadMotoModel> findByIdMoto(Integer idMoto);

    @Query("SELECT m FROM MotoModel m WHERE m.idMoto = :idMoto")
    Optional<MotoModel> findMotoById(@Param("idMoto") Integer idMoto);

    @Query("SELECT COUNT(d) > 0 FROM DisponibilidadMotoModel d WHERE d.idMoto = :idMoto AND d.idTienda = :idTienda")
    boolean isMotoDisponibleEnTienda(@Param("idMoto") Integer idMoto, @Param("idTienda") Integer idTienda);
}
