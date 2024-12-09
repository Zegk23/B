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
            boolean reservaExistente = reservaMotosRepository.existsByMotoAndTiendaAndEstado(
                    motoOpt.get(), tiendaOpt.get(), "PENDIENTE");

            if (reservaExistente) {
                throw new RuntimeException("Esta moto ya está reservada en la tienda seleccionada.");
            }

            ReservaMotosModel reserva = new ReservaMotosModel();
            reserva.setMoto(motoOpt.get());
            reserva.setTienda(tiendaOpt.get());
            reserva.setUsuario(usuarioOpt.get());
            reserva.setEstado("PENDIENTE");

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
    
        // Formatear la fecha en el backend
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaFormateada = dateFormat.format(reserva.getFecha());
        context.setVariable("fechaReserva", fechaFormateada);
    
        try {
            emailService.sendEmail(reserva.getUsuario().getCorreoElectronico(), subject, templateName, context);
        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo de confirmación de reserva: " + e.getMessage());
        }
    }
    

    private void enviarCorreoCancelacionReserva(ReservaMotosModel reserva) {
        String subject = "Cancelación de Reserva - Mass Motos";
        String templateName = "correoReservaCancelada";

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
            System.err.println("Error al enviar el correo de cancelación de reserva: " + e.getMessage());
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

    public void cancelarReserva(int idReserva) {
        Optional<ReservaMotosModel> reservaOpt = reservaMotosRepository.findById(idReserva);
        if (reservaOpt.isPresent()) {
            ReservaMotosModel reserva = reservaOpt.get();
            reserva.setEstado("CANCELADO");

            MotoModel moto = reserva.getMoto();
            moto.setDisponibilidad("Disponible");
            motoRepository.save(moto);

            reservaMotosRepository.save(reserva);

            enviarCorreoCancelacionReserva(reserva);
        } else {
            throw new RuntimeException("Reserva no encontrada");
        }
    }

    public void actualizarEstado(int idReserva, String nuevoEstado) {
        Optional<ReservaMotosModel> reservaOpt = reservaMotosRepository.findById(idReserva);
        if (reservaOpt.isPresent()) {
            ReservaMotosModel reserva = reservaOpt.get();
            reserva.setEstado(nuevoEstado);
            reservaMotosRepository.save(reserva);
        } else {
            throw new RuntimeException("Reserva no encontrada");
        }
    }

    public List<ReservaMotosModel> listarTodasLasReservas() {
        return reservaMotosRepository.findAll();
    }

    public void enviarCorreoCancelacion(int idReserva) {
        Optional<ReservaMotosModel> reservaOpt = reservaMotosRepository.findById(idReserva);
        if (reservaOpt.isPresent()) {
            ReservaMotosModel reserva = reservaOpt.get();
    
            String subject = "Cancelación de Reserva - Mass Motos";
            String templateName = "correoReservaCancelada";
    
            Context context = new Context();
            context.setVariable("usuarioNombreCompleto", reserva.getUsuario().getNombre() + " " +
                    reserva.getUsuario().getApellidoPaterno() + " " +
                    reserva.getUsuario().getApellidoMaterno());
            context.setVariable("motoNombre", reserva.getMoto().getNombreMoto());
            context.setVariable("motoModelo", reserva.getMoto().getModeloMoto());
            context.setVariable("fechaReserva", new SimpleDateFormat("yyyy-MM-dd").format(reserva.getFecha()));
    
            try {
                emailService.sendEmail(reserva.getUsuario().getCorreoElectronico(), subject, templateName, context);
            } catch (MessagingException e) {
                throw new RuntimeException("Error al enviar el correo de cancelación: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("Reserva no encontrada para el ID: " + idReserva);
        }
    }
    
}
