package com.massmotosperu.backend.Services;

import com.massmotosperu.backend.Models.MotoModel;
import com.massmotosperu.backend.Repositories.MotoRepository;
import com.massmotosperu.backend.Repositories.ReservaMotosRepository;
import com.massmotosperu.backend.Exceptions.MotoNoEncontradaException;
import com.massmotosperu.backend.Exceptions.DatosNoDisponiblesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MotoService {

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private ReservaMotosRepository reservaMotosRepository;

    public List<MotoModel> obtenerMotos() {
        List<MotoModel> motos = motoRepository.findAll();
        if (motos.isEmpty()) {
            throw new DatosNoDisponiblesException("No hay motos registradas en la base de datos.");
        }
        return motos;
    }

    public Optional<MotoModel> obtenerMotoPorId(Integer id) {
        return motoRepository.findById(id).or(() -> {
            throw new MotoNoEncontradaException("La moto con ID " + id + " no fue encontrada.");
        });
    }

    public Map<String, Object> calcularMetricasDashboard() {
        Map<String, Object> metrics = new HashMap<>();

        long totalMotos = motoRepository.count();
        if (totalMotos == 0) {
            throw new DatosNoDisponiblesException("No hay motos registradas en la base de datos.");
        }

        long motosDisponibles = motoRepository.countByDisponibilidad("Disponible");

        long motosReservadas = reservaMotosRepository.countByEstado("PENDIENTE");

        List<String> todasLasMarcas = motoRepository.findAllDistinctMarcas();
        if (todasLasMarcas.isEmpty()) {
            throw new DatosNoDisponiblesException("No hay marcas disponibles en las motos.");
        }

        List<Object[]> reservasPorMarca = motoRepository.countReservasPorMarca();
        Map<String, Long> reservasPorMarcaMap = new HashMap<>();
        for (String marca : todasLasMarcas) {
            reservasPorMarcaMap.put(marca, 0L);
        }
        for (Object[] row : reservasPorMarca) {
            reservasPorMarcaMap.put((String) row[0], (Long) row[1]);
        }

        Double promedioPrecioDisponible = motoRepository.findAveragePriceByDisponibilidad("Disponible");
        if (promedioPrecioDisponible == null) {
            promedioPrecioDisponible = 0.0;
        }

        metrics.put("totalMotos", totalMotos);
        metrics.put("motosDisponibles", motosDisponibles);
        metrics.put("motosReservadas", motosReservadas);
        metrics.put("reservasPorMarca", reservasPorMarcaMap);
        metrics.put("promedioPrecioDisponible", promedioPrecioDisponible);

        return metrics;
    }
}
