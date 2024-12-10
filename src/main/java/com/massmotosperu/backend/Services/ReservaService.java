package com.massmotosperu.backend.Services;

import com.massmotosperu.backend.Models.ReservaMotosModel;
import com.massmotosperu.backend.Models.MotoModel;
import com.massmotosperu.backend.Models.SedeTiendaModel;
import com.massmotosperu.backend.Models.UsuarioModel;
import com.massmotosperu.backend.Repositories.ReservaMotosRepository;
import com.massmotosperu.backend.Repositories.MotoRepository;
import com.massmotosperu.backend.Repositories.SedeTiendaRepository;
import com.massmotosperu.backend.Repositories.UsuarioRepository;
import com.massmotosperu.backend.Exceptions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import org.thymeleaf.context.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        MotoModel moto = motoRepository.findById(idMoto)
                .orElseThrow(() -> new MotoNoEncontradaException("La moto con ID " + idMoto + " no fue encontrada."));
        SedeTiendaModel tienda = sedeTiendaRepository.findById(Integer.parseInt(idTienda))
                .orElseThrow(() -> new TiendaNoEncontradaException("La tienda con ID " + idTienda + " no fue encontrada."));
        UsuarioModel usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException(
                        "El usuario con ID " + idUsuario + " no fue encontrado."));

        if (reservaMotosRepository.existsByMotoAndTiendaAndEstado(moto, tienda, "PENDIENTE")) {
            throw new MotoYaReservadaException("Esta moto ya está reservada en la tienda seleccionada.");
        }

        ReservaMotosModel reserva = new ReservaMotosModel();
        reserva.setMoto(moto);
        reserva.setTienda(tienda);
        reserva.setUsuario(usuario);
        reserva.setEstado("PENDIENTE");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = dateFormat.parse(fechaReserva);
        reserva.setFecha(parsedDate);

        ReservaMotosModel reservaGuardada = reservaMotosRepository.save(reserva);
        enviarCorreoConfirmacionReserva(reservaGuardada);

        return reservaGuardada;
    }

    public void cancelarReserva(int idReserva) {
        ReservaMotosModel reserva = reservaMotosRepository.findById(idReserva)
                .orElseThrow(() -> new IDReservaNoEncontradaException(
                        "La reserva con ID " + idReserva + " no fue encontrada."));

        // Actualizar estado de la reserva
        reserva.setEstado("CANCELADO");

        // Actualizar disponibilidad de la moto asociada
        MotoModel moto = reserva.getMoto();
        moto.setDisponibilidad("Disponible");
        motoRepository.save(moto);

        // Guardar la reserva con el nuevo estado
        reservaMotosRepository.save(reserva);
    }

    public List<ReservaMotosModel> obtenerReservasPorUsuario(int idUsuario) {
        UsuarioModel usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException(
                        "El usuario con ID " + idUsuario + " no fue encontrado."));

        return reservaMotosRepository.findByUsuario(usuario);
    }

    public List<ReservaMotosModel> listarTodasLasReservas() {
        List<ReservaMotosModel> reservas = reservaMotosRepository.findAll();
        if (reservas.isEmpty()) {
            throw new DatosNoDisponiblesException("No hay reservas registradas.");
        }
        return reservas;
    }

    public void actualizarEstado(int idReserva, String nuevoEstado) {
        ReservaMotosModel reserva = reservaMotosRepository.findById(idReserva)
                .orElseThrow(() -> new IDReservaNoEncontradaException(
                        "La reserva con ID " + idReserva + " no fue encontrada."));

        reserva.setEstado(nuevoEstado);
        reservaMotosRepository.save(reserva);
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaFormateada = dateFormat.format(reserva.getFecha());
        context.setVariable("fechaReserva", fechaFormateada);

        try {
            emailService.sendEmail(reserva.getUsuario().getCorreoElectronico(), subject, templateName, context);
        } catch (MessagingException e) {
            throw new ErrorEnvioCorreoException("Error al enviar el correo de confirmación: " + e.getMessage());
        }
    }

    public void enviarCorreoCancelacion(int idReserva) {
        ReservaMotosModel reserva = reservaMotosRepository.findById(idReserva)
                .orElseThrow(() -> new IDReservaNoEncontradaException(
                        "La reserva con ID " + idReserva + " no fue encontrada."));

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
            throw new ErrorEnvioCorreoException("Error al enviar el correo de cancelación: " + e.getMessage());
        }
    }

   public List<Map<String, Object>> obtenerReservasYEstadosPorUsuario(int idUsuario) {
    UsuarioModel usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new UsuarioNoEncontradoException(
                    "El usuario con ID " + idUsuario + " no fue encontrado."));

    List<ReservaMotosModel> reservas = reservaMotosRepository.findByUsuario(usuario);

    if (reservas.isEmpty()) {
        throw new DatosNoDisponiblesException("No hay reservas registradas para el usuario.");
    }

    // Mapeamos cada reserva para incluir el estado y otros detalles
    return reservas.stream().map(reserva -> {
        Map<String, Object> reservaMap = new HashMap<>();
        reservaMap.put("idReserva", reserva.getIdReserva());
        reservaMap.put("estado", reserva.getEstado());
        reservaMap.put("fechaReserva", reserva.getFecha().toString());
        reservaMap.put("motoNombre", reserva.getMoto().getNombreMoto());
        reservaMap.put("tiendaNombre", reserva.getTienda().getNombreTienda());
        return reservaMap;
    }).collect(Collectors.toList());
}

}
