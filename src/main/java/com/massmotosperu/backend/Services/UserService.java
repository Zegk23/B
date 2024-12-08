package com.massmotosperu.backend.Services;

import com.massmotosperu.backend.Exceptions.UsuarioYaExisteException;
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
        // Verificar que el usuario existe
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado para el ID: " + usuarioId);
        }

        // Verificar que el rol existe
        // (si tienes un repositorio de roles, puedes validarlo aquí)
        // Ejemplo: rolRepository.findById(rolId).orElseThrow(() -> ...);

        UsuarioRolModel usuarioRol = new UsuarioRolModel(usuarioId, rolId, null, null);
        usuarioRolRepository.save(usuarioRol);
    }

    // Registrar un nuevo usuario
    public UsuarioModel registrarUsuario(@Valid UsuarioModel usuario) {
        // Verificar si el correo ya está registrado
        if (usuarioRepository.findByCorreoElectronico(usuario.getCorreoElectronico()).isPresent()) {
            throw new UsuarioYaExisteException(
                    "El correo electrónico ya está registrado: " + usuario.getCorreoElectronico());
        }

        // Encriptar la contraseña antes de guardar
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));

        // Guardar el usuario en la base de datos
        UsuarioModel savedUser = usuarioRepository.save(usuario);

        // Asignar rol por defecto (1: Usuario) al nuevo usuario
        asignarRol(savedUser.getIdUsuario(), 1);

        // Enviar correo de bienvenida
        enviarCorreoBienvenida(savedUser);

        return savedUser;
    }

    // Enviar correo de bienvenida al usuario
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

    // Verificar las credenciales del usuario
    public Optional<UsuarioModel> verificarUsuario(String correoElectronico, String contraseña) {
        Optional<UsuarioModel> usuario = usuarioRepository.findByCorreoElectronico(correoElectronico);
        if (usuario.isPresent() && passwordEncoder.matches(contraseña, usuario.get().getContraseña())) {
            return usuario;
        }
        return Optional.empty();
    }

    // Obtener un usuario por su ID
    public Optional<UsuarioModel> obtenerUsuarioPorId(int idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    // Actualizar datos del usuario
    public void actualizarUsuario(UsuarioModel usuario) {
        usuarioRepository.save(usuario);
    }

    // Actualizar la contraseña del usuario
    public void actualizarContrasena(UsuarioModel usuario, String nuevaContrasena) {
        usuario.setContraseña(passwordEncoder.encode(nuevaContrasena));
        usuarioRepository.save(usuario);
    }
}
