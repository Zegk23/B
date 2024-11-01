package com.massmotosperu.backend.Controllers;

import com.massmotosperu.backend.Models.MotoModel;
import com.massmotosperu.backend.Services.MotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<MotoModel> obtenerMotoPorNombre(@PathVariable String nombre) {
        return motoService.obtenerMotoPorNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}
