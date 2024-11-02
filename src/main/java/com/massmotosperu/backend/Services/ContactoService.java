package com.massmotosperu.backend.Services;

import com.massmotosperu.backend.Models.ContactoModel;
import com.massmotosperu.backend.Repositories.ContactoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactoService {

    @Autowired
    private ContactoRepository contactoRepository;


    // Guardar la informacion de contacto en la DB
    public ContactoModel guardarContacto(ContactoModel contacto) {
        return contactoRepository.save(contacto);
    }
}
