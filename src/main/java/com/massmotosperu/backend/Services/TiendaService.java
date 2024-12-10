package com.massmotosperu.backend.Services;

import com.massmotosperu.backend.Exceptions.TiendaNoEncontradaException;
import com.massmotosperu.backend.Repositories.SedeTiendaRepository;
import com.massmotosperu.backend.Models.SedeTiendaModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TiendaService {

    @Autowired
    private SedeTiendaRepository sedeTiendaRepository;

    public List<SedeTiendaModel> listarTodasLasTiendas() {
        List<SedeTiendaModel> tiendas = sedeTiendaRepository.findAll();

        if (tiendas.isEmpty()) {
            throw new TiendaNoEncontradaException("No se encontraron tiendas disponibles en el sistema.");
        }

        return tiendas;
    }

    public Map<String, Object> obtenerMetricasTiendas() {
        Map<String, Object> metrics = new HashMap<>();

        long tiendasActivas = sedeTiendaRepository.countActiveStores();
        if (tiendasActivas == 0) {
            throw new TiendaNoEncontradaException("No hay tiendas activas en el sistema.");
        }
        metrics.put("tiendasActivas", tiendasActivas);

        List<Object[]> disponiblesVsReservadas = sedeTiendaRepository.motosDisponiblesVsReservadasPorTienda();
        if (disponiblesVsReservadas.isEmpty()) {
            throw new TiendaNoEncontradaException("No hay datos de motos disponibles o reservadas por tienda.");
        }

        List<Map<String, Object>> tablaDatos = disponiblesVsReservadas.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("tienda", row[0]); // Nombre de la tienda
            map.put("disponibles", ((Number) row[1]).intValue()); // Motos disponibles
            map.put("reservadas", ((Number) row[2]).intValue()); // Motos reservadas
            return map;
        }).collect(Collectors.toList());
        metrics.put("disponiblesVsReservadas", tablaDatos);

        List<Object[]> reservasPorMarca = sedeTiendaRepository.countReservasPorMarca();
        if (reservasPorMarca.isEmpty()) {
            throw new TiendaNoEncontradaException("No hay datos de reservas por marca en las tiendas.");
        }

        Map<String, Integer> distribucionPorMarca = new HashMap<>();
        reservasPorMarca.forEach(row -> {
            distribucionPorMarca.put((String) row[0], ((Number) row[1]).intValue());
        });
        metrics.put("marcaDistribucion", distribucionPorMarca);

        return metrics;
    }
}
