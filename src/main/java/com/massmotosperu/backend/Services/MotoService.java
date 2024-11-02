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

    // Metodo para listar todas las motos de la db
    public List<MotoModel> obtenerMotos() {
        return motoRepository.findAll();
    }

    // Metodo para optener la moto por nombre
    public Optional<MotoModel> obtenerMotoPorNombre(String nombre) {
        return motoRepository.findByNombreMoto(nombre);
    }

    // Metodo para obtener la moto por ID
    public Optional<MotoModel> obtenerMotoPorId(Integer id) {  
        return motoRepository.findById(id);
    }

    // Metodo para eliminar moto por id
    public boolean eliminarMoto(Integer id) {  
        return motoRepository.findById(id).map(moto -> {
            motoRepository.delete(moto);
            return true;
        }).orElse(false);
    }
}
