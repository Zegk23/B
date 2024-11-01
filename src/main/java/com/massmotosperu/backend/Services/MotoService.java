package com.massmotosperu.backend.Services;

import com.massmotosperu.backend.Models.MotoModel;
import com.massmotosperu.backend.Repositories.MotoRepository;
    import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MotoService {

    private final MotoRepository motoRepository;

    public MotoService(MotoRepository motoRepository) {
        this.motoRepository = motoRepository;
    }

    public List<MotoModel> obtenerMotos() {
        return motoRepository.findAll();
    }

    public Optional<MotoModel> obtenerMotoPorNombre(String nombre) {
        return motoRepository.findByNombreMoto(nombre);
    }

    public Optional<MotoModel> obtenerMotoPorId(Integer id) {  
        return motoRepository.findById(id);
    }

    public boolean eliminarMoto(Integer id) {  
        return motoRepository.findById(id).map(moto -> {
            motoRepository.delete(moto);
            return true;
        }).orElse(false);
    }
}
