package com.massmotosperu.backend.Services;

import com.massmotosperu.backend.Exceptions.UsuarioNoEncontradoException;
import com.massmotosperu.backend.Exceptions.UsuarioYaExisteException;
import com.massmotosperu.backend.Exceptions.CredencialesInvalidasException;
import com.massmotosperu.backend.Models.UsuarioModel;
import com.massmotosperu.backend.Models.UsuarioRolModel;
import com.massmotosperu.backend.Repositories.UsuarioRepository;
import com.massmotosperu.backend.Repositories.UsuarioRolRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.validation.Valid;
import java.util.Optional;

@Service
public class UserService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UsuarioRepository usuarioRepository,
                       UsuarioRolRepository usuarioRolRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    // Método para asignar rol a un usuario
    public void asignarRol(int usuarioId, int rolId) {
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado para el ID: " + usuarioId);
        }

        UsuarioRolModel usuarioRol = new UsuarioRolModel(usuarioId, rolId, null, null);
        usuarioRolRepository.save(usuarioRol);
    }

    // Registrar un nuevo usuario
    public UsuarioModel registrarUsuario(@Valid UsuarioModel usuario) {
        if (usuarioRepository.findByCorreoElectronico(usuario.getCorreoElectronico()).isPresent()) {
            throw new UsuarioYaExisteException(
                    "El correo electrónico ya está registrado: " + usuario.getCorreoElectronico());
        }

        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));

        UsuarioModel savedUser = usuarioRepository.save(usuario);

        asignarRol(savedUser.getIdUsuario(), 1);

        enviarCorreoBienvenida(savedUser);

        return savedUser;
    }

    // Enviar correo de bienvenida
    private void enviarCorreoBienvenida(UsuarioModel usuario) {
        String subject = "Bienvenido a Mass Motos";
        String templateName = "correo";
        Context context = new Context();
        context.setVariable("nombre", usuario.getNombre());

        try {
            emailService.sendEmail(usuario.getCorreoElectronico(), subject, templateName, context);
        } catch (Exception e) {
            System.err.println("Error al enviar el correo de bienvenida: " + e.getMessage());
        }
    }

    // Enviar correo al iniciar sesión
    public void enviarCorreoInicioSesion(UsuarioModel usuario) {
        String subject = "Inicio de Sesión en Mass Motos";
        String templateName = "correoLogin";
        Context context = new Context();
        context.setVariable("nombre", usuario.getNombre());

        try {
            emailService.sendEmail(usuario.getCorreoElectronico(), subject, templateName, context);
        } catch (Exception e) {
            System.err.println("Error al enviar el correo de inicio de sesión: " + e.getMessage());
        }
    }

    public Optional<UsuarioModel> verificarUsuario(String correoElectronico, String contraseña) {
        Optional<UsuarioModel> usuario = usuarioRepository.findByCorreoElectronico(correoElectronico);
        if (usuario.isEmpty() || !passwordEncoder.matches(contraseña, usuario.get().getContraseña())) {
            throw new CredencialesInvalidasException("Correo electrónico o contraseña incorrectos.");
        }
        return usuario;
    }

    public Optional<UsuarioModel> obtenerUsuarioPorId(int idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .or(() -> {
                    throw new UsuarioNoEncontradoException("Usuario no encontrado para el ID: " + idUsuario);
                });
    }

    public void actualizarUsuario(UsuarioModel usuario) {
        if (!usuarioRepository.existsById(usuario.getIdUsuario())) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado para el ID: " + usuario.getIdUsuario());
        }
        usuarioRepository.save(usuario);
    }

    public void actualizarContrasena(UsuarioModel usuario, String nuevaContrasena) {
        if (!usuarioRepository.existsById(usuario.getIdUsuario())) {
            throw new UsuarioNoEncontradoException("Usuario no encontrado para el ID: " + usuario.getIdUsuario());
        }
        usuario.setContraseña(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);
    }
}
