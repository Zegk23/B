package com.massmotosperu.backend.Services;

import com.massmotosperu.backend.Exceptions.*;
import com.massmotosperu.backend.Models.ContactoModel;
import com.massmotosperu.backend.Repositories.ContactoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ContactoService {

    @Autowired
    private ContactoRepository contactoRepository;

    public ContactoModel guardarContacto(ContactoModel contacto) {
        if (contacto.getCorreoElectronico() == null || contacto.getCorreoElectronico().isEmpty()) {
            throw new RecursoAsociadoException("El correo electrónico es obligatorio.");
        }

        return contactoRepository.save(contacto);
    }
}
