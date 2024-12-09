package com.massmotosperu.backend.Services;

import com.massmotosperu.backend.Models.MotoModel;
import com.massmotosperu.backend.Models.ReservaMotosModel;
import com.massmotosperu.backend.Models.SedeTiendaModel;
import com.massmotosperu.backend.Repositories.MotoRepository;
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
public class MotoService {

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private SedeTiendaRepository sedeTiendaRepository;

    @Autowired
    private ReservaMotosRepository reservaMotosRepository;

    public List<MotoModel> obtenerMotos() {
        return motoRepository.findAll();
    }

    public Optional<MotoModel> obtenerMotoPorId(Integer id) {
        return motoRepository.findById(id);
    }

    public String verificarDisponibilidadMoto(Integer idMoto) {
        Optional<MotoModel> motoOpt = motoRepository.findById(idMoto);

        if (motoOpt.isPresent()) {
            MotoModel moto = motoOpt.get();

            if ("Disponible".equalsIgnoreCase(moto.getDisponibilidad())) {
                // Busca las tiendas donde está disponible esta moto
                List<SedeTiendaModel> tiendasConMoto = sedeTiendaRepository.findTiendasByMotoId(idMoto);

                if (tiendasConMoto.isEmpty()) {
                    return "La moto está disponible, pero no está asignada a ninguna tienda.";
                }

                return "La moto está disponible en las siguientes tiendas: " +
                        tiendasConMoto.stream()
                                .map(SedeTiendaModel::getNombreTienda)
                                .collect(Collectors.joining(", "));
            } else {
                // Verifica si la moto está reservada
                List<ReservaMotosModel> reservas = reservaMotosRepository.findByMotoAndEstado(moto, "PENDIENTE");

                if (!reservas.isEmpty()) {
                    return "La moto está reservada en la tienda: " +
                            reservas.get(0).getTienda().getNombreTienda();
                }

                return "La moto no está disponible actualmente.";
            }
        }

        throw new RuntimeException("Moto no encontrada para el ID proporcionado.");
    }

    public List<String> obtenerTiendasDisponibilidadMoto(Integer idMoto) {
        List<SedeTiendaModel> tiendasConMoto = sedeTiendaRepository.findTiendasByMotoId(idMoto);
        return tiendasConMoto.stream()
                .map(SedeTiendaModel::getNombreTienda)
                .collect(Collectors.toList());
    }

    public Map<String, Object> obtenerDetalleCompletoDisponibilidadMoto(Integer idMoto) {
        Optional<MotoModel> motoOpt = motoRepository.findById(idMoto);

        if (motoOpt.isPresent()) {
            MotoModel moto = motoOpt.get();
            Map<String, Object> resultado = new HashMap<>();

            resultado.put("moto", Map.of(
                    "idMoto", moto.getIdMoto(),
                    "nombre", moto.getNombreMoto(),
                    "marca", moto.getMarcaMoto(),
                    "modelo", moto.getModeloMoto(),
                    "estado", moto.getEstadoMoto()));

            // Buscar todas las tiendas donde la moto está asociada
            List<SedeTiendaModel> tiendasConMoto = sedeTiendaRepository.findTiendasByMotoId(idMoto);

            // Construir el detalle por tienda
            List<Map<String, Object>> detallesPorTienda = tiendasConMoto.stream().map(tienda -> {
                Map<String, Object> detalleTienda = new HashMap<>();
                detalleTienda.put("nombreTienda", tienda.getNombreTienda());
                detalleTienda.put("ubicacion", tienda.getUbicacion());
                detalleTienda.put("telefono", tienda.getTelefonoTienda());

                // Verificar si la moto está reservada en esta tienda
                List<ReservaMotosModel> reservas = reservaMotosRepository.findByMotoAndTiendaAndEstado(moto, tienda,
                        "PENDIENTE");
                if (!reservas.isEmpty()) {
                    detalleTienda.put("estado", "Reservada");
                } else if ("Disponible".equalsIgnoreCase(moto.getDisponibilidad())) {
                    detalleTienda.put("estado", "Disponible");
                } else {
                    detalleTienda.put("estado", "No Disponible");
                }

                return detalleTienda;
            }).collect(Collectors.toList());

            resultado.put("disponibilidadPorTienda", detallesPorTienda);

            return resultado;
        }

        throw new RuntimeException("Moto no encontrada para el ID proporcionado.");
    }

    public Map<String, Object> calcularMetricasDashboard() {
        Map<String, Object> metrics = new HashMap<>();

        // Total de motocicletas
        long totalMotos = motoRepository.count();

        // Número de motocicletas disponibles
        long motosDisponibles = motoRepository.countByDisponibilidad("Disponible");

        // Número de motocicletas reservadas
        long motosReservadas = reservaMotosRepository.countByEstado("PENDIENTE");

        // Obtener todas las marcas
        List<String> todasLasMarcas = motoRepository.findAllDistinctMarcas();

        // Obtener reservas por marca
        List<Object[]> reservasPorMarca = reservaMotosRepository.countReservasPorMarca();
        Map<String, Long> reservasPorMarcaMap = new HashMap<>();
        for (String marca : todasLasMarcas) {
            reservasPorMarcaMap.put(marca, 0L); // Inicializar con 0
        }
        for (Object[] row : reservasPorMarca) {
            reservasPorMarcaMap.put((String) row[0], (Long) row[1]);
        }

        // Promedio de precios de las motos disponibles
        Double promedioPrecioDisponible = motoRepository.findAveragePriceByDisponibilidad("Disponible");

        metrics.put("totalMotos", totalMotos);
        metrics.put("motosDisponibles", motosDisponibles);
        metrics.put("motosReservadas", motosReservadas);
        metrics.put("reservasPorMarca", reservasPorMarcaMap);
        metrics.put("promedioPrecioDisponible", promedioPrecioDisponible);

        return metrics;
    }

}
