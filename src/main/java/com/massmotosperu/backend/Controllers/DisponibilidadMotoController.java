package com.massmotosperu.backend.Controllers;

import com.massmotosperu.backend.Models.SedeTiendaModel;
import com.massmotosperu.backend.Services.DisponibilidadMotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disponibilidad")
@CrossOrigin(origins = "http://localhost:3000")
public class DisponibilidadMotoController {

    @Autowired
    private DisponibilidadMotoService disponibilidadMotoService;

    /**
     * Endpoint para obtener una lista de tiendas donde una moto está disponible.
     *
     * @param idMoto El ID de la moto a verificar.
     * @return ResponseEntity con la lista de tiendas donde la moto está disponible.
     */
    @GetMapping("/moto/{idMoto}")
    public ResponseEntity<List<SedeTiendaModel>> getTiendasDisponiblesPorMoto(@PathVariable Integer idMoto) {
        List<SedeTiendaModel> tiendasDisponibles = disponibilidadMotoService.getTiendasDisponiblesPorMoto(idMoto);
        return ResponseEntity.ok(tiendasDisponibles);
    }
}
