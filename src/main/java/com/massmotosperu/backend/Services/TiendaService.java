package com.massmotosperu.backend.Services;

import com.massmotosperu.backend.Models.ReservaMotosModel;
import com.massmotosperu.backend.Models.SedeTiendaModel;
import com.massmotosperu.backend.Models.MotoModel;
import com.massmotosperu.backend.Repositories.ReservaMotosRepository;
import com.massmotosperu.backend.Repositories.SedeTiendaRepository;
import com.massmotosperu.backend.Repositories.MotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TiendaService {

    @Autowired
    private SedeTiendaRepository sedeTiendaRepository;

    @Autowired
    private ReservaMotosRepository reservaMotosRepository;

    @Autowired
    private MotoRepository motoRepository;

    public List<SedeTiendaModel> listarTodasLasTiendas() {
        return sedeTiendaRepository.findAll();
    }


    public List<SedeTiendaModel> obtenerTiendasConMotoDisponible(int idMoto) {
        Optional<MotoModel> motoOpt = motoRepository.findById(idMoto);
        if (motoOpt.isPresent()) {
            MotoModel moto = motoOpt.get();
            List<ReservaMotosModel> reservas = reservaMotosRepository.findByMoto(moto);

            Set<Integer> sedesIds = new HashSet<>();
            List<SedeTiendaModel> sedesUnicas = reservas.stream()
                .map(ReservaMotosModel::getTienda)
                .filter(tienda -> sedesIds.add(tienda.getIdTienda())) 
                .collect(Collectors.toList());

            return sedesUnicas;
        }
        return List.of();
    }
}
