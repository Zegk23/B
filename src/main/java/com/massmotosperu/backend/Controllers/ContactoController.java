package com.massmotosperu.backend.Controllers;

import com.massmotosperu.backend.Models.ContactoModel;
import com.massmotosperu.backend.Services.ContactoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacto")
@CrossOrigin(origins = "http://localhost:3000")
public class ContactoController {

    @Autowired
    private ContactoService contactoService;

    @PostMapping("/enviar")
    public ResponseEntity<ContactoModel> enviarContacto(@RequestBody ContactoModel contacto) {
        ContactoModel nuevoContacto = contactoService.guardarContacto(contacto);
        return ResponseEntity.ok(nuevoContacto);
    }
}
