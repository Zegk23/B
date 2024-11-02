package com.massmotosperu.backend.Controllers;

import com.massmotosperu.backend.Models.SedeTiendaModel;
import com.massmotosperu.backend.Services.TiendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/tiendas")
public class TiendaController {

    @Autowired
    private TiendaService tiendaService;

    @GetMapping
    public List<SedeTiendaModel> listarTodasLasTiendas() {
        return tiendaService.listarTodasLasTiendas();
    }
}
