package com.massmotosperu.backend.Services;

import com.massmotosperu.backend.Models.ReservaMotosModel;
import com.massmotosperu.backend.Models.MotoModel;
import com.massmotosperu.backend.Models.SedeTiendaModel;
import com.massmotosperu.backend.Models.UsuarioModel;
import com.massmotosperu.backend.Repositories.ReservaMotosRepository;
import com.massmotosperu.backend.Repositories.MotoRepository;
import com.massmotosperu.backend.Repositories.SedeTiendaRepository;
import com.massmotosperu.backend.Repositories.UsuarioRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import org.thymeleaf.context.Context;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Service
public class ReservaService {

    @Autowired
    private ReservaMotosRepository reservaMotosRepository;

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private SedeTiendaRepository sedeTiendaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    public ReservaMotosModel crearReserva(int idMoto, String idTienda, int idUsuario, String fechaReserva)
            throws ParseException {
        Optional<MotoModel> motoOpt = motoRepository.findById(idMoto);
        Optional<SedeTiendaModel> tiendaOpt = sedeTiendaRepository.findById(Integer.parseInt(idTienda));
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (motoOpt.isPresent() && tiendaOpt.isPresent() && usuarioOpt.isPresent()) {
            boolean reservaExistente = reservaMotosRepository.existsByMotoAndTienda(motoOpt.get(), tiendaOpt.get());

            if (reservaExistente) {
                throw new RuntimeException("Esta moto ya está reservada en la tienda seleccionada.");
            }

            ReservaMotosModel reserva = new ReservaMotosModel();
            reserva.setMoto(motoOpt.get());
            reserva.setTienda(tiendaOpt.get());
            reserva.setUsuario(usuarioOpt.get());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(fechaReserva);
            reserva.setFecha(parsedDate);

            ReservaMotosModel reservaGuardada = reservaMotosRepository.save(reserva);

            enviarCorreoConfirmacionReserva(reservaGuardada);

            return reservaGuardada;
        } else {
            throw new RuntimeException("Moto, Tienda o Usuario no encontrado");
        }
    }

    private void enviarCorreoConfirmacionReserva(ReservaMotosModel reserva) {
        String subject = "Confirmación de Reserva - Mass Motos";
        String templateName = "correoReserva"; 

        Context context = new Context();
        context.setVariable("usuarioNombreCompleto", reserva.getUsuario().getNombre() + " " +
                reserva.getUsuario().getApellidoPaterno() + " " +
                reserva.getUsuario().getApellidoMaterno());
        context.setVariable("motoNombre", reserva.getMoto().getNombreMoto());
        context.setVariable("motoModelo", reserva.getMoto().getModeloMoto());
        context.setVariable("fechaReserva", reserva.getFecha().toString());

        try {
            emailService.sendEmail(reserva.getUsuario().getCorreoElectronico(), subject, templateName, context);
        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo de confirmación de reserva: " + e.getMessage());
        }
    }
    public List<ReservaMotosModel> obtenerReservasPorUsuario(int idUsuario) {
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (usuarioOpt.isPresent()) {
            return reservaMotosRepository.findByUsuario(usuarioOpt.get());
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }

    // Cancelar (eliminar) una reserva
    public void cancelarReserva(int idReserva) {
        Optional<ReservaMotosModel> reservaOpt = reservaMotosRepository.findById(idReserva);
        if (reservaOpt.isPresent()) {
            reservaMotosRepository.delete(reservaOpt.get());
        } else {
            throw new RuntimeException("Reserva no encontrada");
        }
    }
}
