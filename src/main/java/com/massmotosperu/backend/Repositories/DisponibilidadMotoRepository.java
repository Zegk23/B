package com.massmotosperu.backend.Repositories;

import com.massmotosperu.backend.Models.DisponibilidadMotoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibilidadMotoRepository extends JpaRepository<DisponibilidadMotoModel, Integer> {
    
    List<DisponibilidadMotoModel> findByIdMoto(Integer idMoto);
}
