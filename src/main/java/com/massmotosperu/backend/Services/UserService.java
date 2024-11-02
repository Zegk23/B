package com.massmotosperu.backend.Services;

import com.massmotosperu.backend.DTOs.ActualizacionDatosDTO;
import com.massmotosperu.backend.Exceptions.UsuarioYaExisteException;
import com.massmotosperu.backend.Models.UsuarioModel;
import com.massmotosperu.backend.Repositories.UsuarioRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.validation.Valid;
import java.util.Optional;

@Service
public class UserService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // Metodo para obtener al usuario por su id
    public Optional<UsuarioModel> obtenerUsuarioPorId(int idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    public UserService(UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    // Metodo para registrar a un usuario
    public UsuarioModel registrarUsuario(@Valid UsuarioModel usuario) {
        if (usuarioRepository.findByCorreoElectronico(usuario.getCorreoElectronico()).isPresent()) {
            throw new UsuarioYaExisteException(
                    "El correo electrónico ya está registrado: " + usuario.getCorreoElectronico());
        }

        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        UsuarioModel savedUser = usuarioRepository.save(usuario);
        enviarCorreoBienvenida(savedUser);
        return savedUser;
    }

    // METODO PARA ENVIAR CORREO CUANDO SE REGISTRE UN USUARIO
    private void enviarCorreoBienvenida(UsuarioModel usuario) {
        String subject = "Bienvenido a Mass Motos";
        String templateName = "correo";
        Context context = new Context();
        context.setVariable("nombre", usuario.getNombre());

        try {
            emailService.sendEmail(usuario.getCorreoElectronico(), subject, templateName, context);
        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo de bienvenida: " + e.getMessage());
        }
    }

    // Metodo para enviar correo cuando se inicie sesion
    public void enviarCorreoInicioSesionExitoso(UsuarioModel usuario) {
        String subject = "Inicio de Sesión Exitoso";
        String templateName = "correoLogin";
        Context context = new Context();
        context.setVariable("nombre", usuario.getNombre());

        try {
            emailService.sendEmail(usuario.getCorreoElectronico(), subject, templateName, context);
        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo de inicio de sesión: " + e.getMessage());
        }
    }

    // Metodo para validar usuario
    public Optional<UsuarioModel> verificarUsuario(String correoElectronico, String contraseña) {
        Optional<UsuarioModel> usuario = usuarioRepository.findByCorreoElectronico(correoElectronico);
        if (usuario.isPresent() && passwordEncoder.matches(contraseña, usuario.get().getContraseña())) {
            enviarCorreoInicioSesionExitoso(usuario.get());
            return usuario;
        }
        return Optional.empty();
    }

    // Metodo para obtener el correo del usuario
    public Optional<UsuarioModel> obtenerUsuarioPorCorreo(String correoElectronico) {
        return usuarioRepository.findByCorreoElectronico(correoElectronico);
    }

    // Metodo apra actualizar los datos del usuario usando DTO(DATA TRANSFER OBJECT)
    public void actualizarDatosUsuario(Integer userId, ActualizacionDatosDTO datosDTO) {
        UsuarioModel usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(datosDTO.getNombre());
        usuario.setApellidoPaterno(datosDTO.getApellidoPaterno());
        usuario.setApellidoMaterno(datosDTO.getApellidoMaterno());
        usuario.setCorreoElectronico(datosDTO.getCorreoElectronico());
        usuario.setTelefono(datosDTO.getTelefono());
        usuario.setEdad(datosDTO.getEdad()); 
        usuario.setDni(datosDTO.getDni());
        usuario.setPreNombre(datosDTO.getPreNombre());

        if (datosDTO.getContraseña() != null && !datosDTO.getContraseña().isEmpty()) {
            usuario.setContraseña(passwordEncoder.encode(datosDTO.getContraseña()));
        }

        usuarioRepository.save(usuario);
    }

    // Metodo para actualziar contraseña
    public void actualizarContrasena(UsuarioModel usuario, String nuevaContrasena) {
        usuario.setContraseña(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);
    }

    // Método agregado para guardar directamente el UsuarioModel
    public void actualizarUsuario(UsuarioModel usuario) {
        usuarioRepository.save(usuario);
    }
}
