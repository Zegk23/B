package com.massmotosperu.backend.Controllers;

import com.massmotosperu.backend.DTOs.ReservaDTO;
import com.massmotosperu.backend.Models.ReservaMotosModel;
import com.massmotosperu.backend.Services.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;

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
}
