package com.massmotosperu.backend.Services;

import com.massmotosperu.backend.Exceptions.MotoNoDisponibleException;
import com.massmotosperu.backend.Exceptions.MotoNoEncontradaException;
import com.massmotosperu.backend.Exceptions.TiendaNoEncontradaException;
import com.massmotosperu.backend.Models.MotoModel;
import com.massmotosperu.backend.Models.SedeTiendaModel;
import com.massmotosperu.backend.Repositories.DisponibilidadMotoRepository;
import com.massmotosperu.backend.Repositories.ReservaMotosRepository;
import com.massmotosperu.backend.Repositories.SedeTiendaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DisponibilidadMotoService {

    @Autowired
    private DisponibilidadMotoRepository disponibilidadMotoRepository;

    @Autowired
    private SedeTiendaRepository sedeTiendaRepository;

    @Autowired
    private ReservaMotosRepository reservaMotosRepository;

    public List<SedeTiendaModel> getTiendasDisponiblesPorMoto(Integer idMoto) {
        List<SedeTiendaModel> tiendas = disponibilidadMotoRepository.findByIdMoto(idMoto).stream()
                .map(d -> sedeTiendaRepository.findById(d.getIdTienda()).orElse(null))
                .filter(tienda -> tienda != null)
                .collect(Collectors.toList());

        if (tiendas.isEmpty()) {
            throw new TiendaNoEncontradaException("No se encontraron tiendas para la moto con ID: " + idMoto);
        }
        return tiendas;
    }

    public Map<String, Object> obtenerDetalleCompletoDisponibilidadMoto(Integer idMoto) {
        Optional<MotoModel> motoOpt = disponibilidadMotoRepository.findMotoById(idMoto);

        if (motoOpt.isEmpty()) {
            throw new MotoNoEncontradaException("La moto con ID: " + idMoto + " no fue encontrada.");
        }

        MotoModel moto = motoOpt.get();
        Map<String, Object> resultado = new HashMap<>();

        resultado.put("moto", Map.of(
                "idMoto", moto.getIdMoto(),
                "nombre", moto.getNombreMoto(),
                "marca", moto.getMarcaMoto(),
                "modelo", moto.getModeloMoto(),
                "estado", moto.getEstadoMoto()));

        List<SedeTiendaModel> tiendasConMoto = sedeTiendaRepository.findTiendasByMotoId(idMoto);

        if (tiendasConMoto.isEmpty()) {
            throw new TiendaNoEncontradaException("No se encontraron tiendas para la moto con ID: " + idMoto);
        }

        List<Map<String, Object>> detallesPorTienda = tiendasConMoto.stream().map(tienda -> {
            Map<String, Object> detalleTienda = new HashMap<>();
            detalleTienda.put("nombreTienda", tienda.getNombreTienda());
            detalleTienda.put("ubicacion", tienda.getUbicacion());
            detalleTienda.put("telefono", tienda.getTelefonoTienda());

            boolean disponible = disponibilidadMotoRepository.isMotoDisponibleEnTienda(idMoto, tienda.getIdTienda());
            boolean reservada = reservaMotosRepository.existsByMotoAndTiendaAndEstado(moto, tienda, "PENDIENTE");

            if (reservada) {
                detalleTienda.put("estado", "Reservada");
            } else if (disponible) {
                detalleTienda.put("estado", "Disponible");
            } else {
                detalleTienda.put("estado", "No Disponible");
            }

            return detalleTienda;
        }).collect(Collectors.toList());

        resultado.put("disponibilidadPorTienda", detallesPorTienda);

        return resultado;
    }
}
