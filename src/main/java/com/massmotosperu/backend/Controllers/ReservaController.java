package com.massmotosperu.backend.Controllers;

import com.massmotosperu.backend.Models.ReservaMotosModel;
import com.massmotosperu.backend.Services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.massmotosperu.backend.DTOs.ReservaDTO;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping("/crear")
    public ReservaMotosModel crearReserva(@RequestBody ReservaDTO reservaDTO) throws ParseException {
        return reservaService.crearReserva(
                reservaDTO.getIdMoto(),
                reservaDTO.getIdTienda(),
                reservaDTO.getIdUsuario(),
                reservaDTO.getFechaReserva());
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<ReservaMotosModel> obtenerReservasPorUsuario(@PathVariable int idUsuario) {
        return reservaService.obtenerReservasPorUsuario(idUsuario);
    }

    @DeleteMapping("/cancelar/{idReserva}")
    public void cancelarReserva(@PathVariable int idReserva) {
        reservaService.cancelarReserva(idReserva);
    }

    @GetMapping("/listarTodas")
    public List<ReservaMotosModel> listarTodasLasReservas() {
        return reservaService.listarTodasLasReservas();
    }

    @PutMapping("/actualizarEstado/{idReserva}")
    public ResponseEntity<?> actualizarEstadoReserva(@PathVariable int idReserva,
            @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        reservaService.actualizarEstado(idReserva, nuevoEstado);
        return ResponseEntity.ok("Estado actualizado correctamente");
    }

    @PostMapping("/enviarCorreoCancelacion/{idReserva}")
    public ResponseEntity<?> enviarCorreoCancelacion(@PathVariable int idReserva) {
        try {
            reservaService.enviarCorreoCancelacion(idReserva);
            return ResponseEntity.ok("Correo de cancelación enviado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al enviar el correo de cancelación: " + e.getMessage());
        }
    }

}
