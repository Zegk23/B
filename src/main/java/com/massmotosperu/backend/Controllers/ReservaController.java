package com.massmotosperu.backend.Controllers;

import com.massmotosperu.backend.Models.ReservaMotosModel;
import com.massmotosperu.backend.Services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.massmotosperu.backend.DTOs.ReservaDTO;
import com.massmotosperu.backend.Exceptions.IDReservaNoEncontradaException;
import com.massmotosperu.backend.Exceptions.UsuarioNoEncontradoException;

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

    @PutMapping("/cancelar/{idReserva}")
    public ResponseEntity<?> cancelarReserva(@PathVariable int idReserva) {
        try {
            reservaService.cancelarReserva(idReserva);
            return ResponseEntity.ok("Reserva cancelada correctamente.");
        } catch (IDReservaNoEncontradaException e) {
            return ResponseEntity.status(404).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
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

    @GetMapping("/usuario/{idUsuario}/reservas")
    public ResponseEntity<?> obtenerReservasYEstadosPorUsuario(@PathVariable int idUsuario) {
        try {
            List<Map<String, Object>> reservas = reservaService.obtenerReservasYEstadosPorUsuario(idUsuario);
            return ResponseEntity.ok(reservas);
        } catch (UsuarioNoEncontradoException e) {
            return ResponseEntity.status(404).body("Usuario no encontrado: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener las reservas: " + e.getMessage());
        }
    }

}
