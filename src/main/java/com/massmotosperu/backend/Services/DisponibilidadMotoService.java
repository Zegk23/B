package com.massmotosperu.backend.Services;

import com.massmotosperu.backend.Models.DisponibilidadMotoModel;
import com.massmotosperu.backend.Models.SedeTiendaModel;  
import com.massmotosperu.backend.Repositories.DisponibilidadMotoRepository;
import com.massmotosperu.backend.Repositories.SedeTiendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DisponibilidadMotoService {

    @Autowired
    private DisponibilidadMotoRepository disponibilidadMotoRepository;

    @Autowired
    private SedeTiendaRepository sedeTiendaRepository;

    /**
     * Obtiene una lista de tiendas donde una moto está disponible.
     * 
     * @param idMoto El ID de la moto a verificar.
     * @return Lista de SedeTiendaModel con la información de las tiendas donde está disponible la moto.
     */
    public List<SedeTiendaModel> getTiendasDisponiblesPorMoto(Integer idMoto) {
        List<DisponibilidadMotoModel> disponibilidad = disponibilidadMotoRepository.findByIdMoto(idMoto);
        return disponibilidad.stream()
                .map(d -> sedeTiendaRepository.findById(d.getIdTienda()).orElse(null))
                .filter(tienda -> tienda != null)
                .collect(Collectors.toList());
    }
}
