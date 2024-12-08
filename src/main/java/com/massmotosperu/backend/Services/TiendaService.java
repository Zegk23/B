package com.massmotosperu.backend.Services;

import com.massmotosperu.backend.Models.SedeTiendaModel;
import com.massmotosperu.backend.Repositories.SedeTiendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TiendaService {

    @Autowired
    private SedeTiendaRepository sedeTiendaRepository;

    public List<SedeTiendaModel> listarTodasLasTiendas() {
        return sedeTiendaRepository.findAll();
    }

}
