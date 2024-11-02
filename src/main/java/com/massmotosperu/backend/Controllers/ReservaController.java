// ReservaController.java
package com.massmotosperu.backend.Controllers;

import com.massmotosperu.backend.Models.ReservaMotosModel;
import com.massmotosperu.backend.Services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.massmotosperu.backend.DTOs.ReservaDTO;
import java.text.ParseException; 

import java.util.List;

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
            reservaDTO.getFechaReserva()
        );
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<ReservaMotosModel> obtenerReservasPorUsuario(@PathVariable int idUsuario) {
        return reservaService.obtenerReservasPorUsuario(idUsuario);
    }

    @DeleteMapping("/cancelar/{idReserva}")
    public void cancelarReserva(@PathVariable int idReserva) {
        reservaService.cancelarReserva(idReserva);
    }
}
