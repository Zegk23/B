package com.massmotosperu.backend.Controllers;

import com.massmotosperu.backend.Models.MotoModel;
import com.massmotosperu.backend.Services.MotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/motos")
@CrossOrigin(origins = "http://localhost:3000")
public class MotoController {

    @Autowired
    private MotoService motoService;

    @GetMapping("/listarMotos")
    public ResponseEntity<List<MotoModel>> listarMotos() {
        return ResponseEntity.ok(motoService.obtenerMotos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotoModel> obtenerMotoPorId(@PathVariable Integer id) {
        return motoService.obtenerMotoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/disponibilidad/{id}")
    public ResponseEntity<?> obtenerDetalleCompletoDisponibilidad(@PathVariable Integer id) {
        try {
            Map<String, Object> detalleCompleto = motoService.obtenerDetalleCompletoDisponibilidadMoto(id);
            return ResponseEntity.ok(detalleCompleto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/dashboard/metrics")
    public ResponseEntity<Map<String, Object>> obtenerMetricaDashboard() {
        Map<String, Object> metrics = motoService.calcularMetricasDashboard();
        return ResponseEntity.ok(metrics);
    }

}
