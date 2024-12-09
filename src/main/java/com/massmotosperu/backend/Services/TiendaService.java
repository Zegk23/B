package com.massmotosperu.backend.Services;

import com.massmotosperu.backend.Models.SedeTiendaModel;
import com.massmotosperu.backend.Repositories.SedeTiendaRepository;
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
        return sedeTiendaRepository.findAll();
    }

    public Map<String, Object> obtenerMetricasTiendas() {
        Map<String, Object> metrics = new HashMap<>();
    
        // Tiendas activas
        metrics.put("tiendasActivas", sedeTiendaRepository.countActiveStores());
    
        // Datos para la tabla (disponibles vs reservadas por tienda)
        List<Object[]> disponiblesVsReservadas = sedeTiendaRepository.motosDisponiblesVsReservadasPorTienda();
        List<Map<String, Object>> tablaDatos = disponiblesVsReservadas.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("tienda", row[0]);  // Nombre de la tienda
            map.put("disponibles", ((Number) row[1]).intValue());  // Motos disponibles
            map.put("reservadas", ((Number) row[2]).intValue());  // Motos reservadas
            return map;
        }).collect(Collectors.toList());
        metrics.put("disponiblesVsReservadas", tablaDatos);
    
        // Distribución de reservas por marcas
        List<Object[]> reservasPorMarca = sedeTiendaRepository.countReservasPorMarca();
        Map<String, Integer> distribucionPorMarca = new HashMap<>();
        reservasPorMarca.forEach(row -> {
            distribucionPorMarca.put((String) row[0], ((Number) row[1]).intValue());
        });
        metrics.put("marcaDistribucion", distribucionPorMarca);
    
        return metrics;
    }
    
}
