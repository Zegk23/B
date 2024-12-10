package com.massmotosperu.backend.Controllers;

import com.massmotosperu.backend.DTOs.ActualizacionDatosDTO;
import com.massmotosperu.backend.DTOs.LoginDTO;
import com.massmotosperu.backend.Models.UsuarioModel;
import com.massmotosperu.backend.Models.UsuarioRolModel;
import com.massmotosperu.backend.Repositories.UsuarioRolRepository;
import com.massmotosperu.backend.Services.UserService;
import com.massmotosperu.backend.Utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UsuarioRolRepository usuarioRolRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody UsuarioModel usuario) {
        try {
            UsuarioModel nuevoUsuario = userService.registrarUsuario(usuario);

            userService.asignarRol(nuevoUsuario.getIdUsuario(), 1);

            return ResponseEntity.ok(Map.of(
                    "id", nuevoUsuario.getIdUsuario(),
                    "mensaje", "Usuario registrado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al registrar el usuario: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            Optional<UsuarioModel> user = userService.verificarUsuario(
                    loginDTO.getCorreoElectronico(),
                    loginDTO.getContraseña());
    
            if (user.isPresent()) {
                UsuarioModel usuario = user.get();
    
                Optional<UsuarioRolModel> usuarioRol = usuarioRolRepository.findByUsuarioID(usuario.getIdUsuario());
                int idRol = usuarioRol.map(UsuarioRolModel::getRolID).orElseThrow(
                        () -> new RuntimeException("Rol no encontrado para el usuario: " + usuario.getIdUsuario()));
    
                String token = jwtUtil.generarToken(Map.of(
                        "userId", usuario.getIdUsuario(),
                        "nombre", usuario.getNombre(),
                        "preNombre", usuario.getPreNombre(),
                        "apellidoPaterno", usuario.getApellidoPaterno(),
                        "apellidoMaterno", usuario.getApellidoMaterno(),
                        "dni", usuario.getDni(),
                        "telefono", usuario.getTelefono(),
                        "correoElectronico", usuario.getCorreoElectronico(),
                        "idRol", idRol));
    
                userService.enviarCorreoInicioSesion(usuario);
    
                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "userId", usuario.getIdUsuario(),
                        "idRol", idRol,
                        "nombre", usuario.getNombre(),
                        "preNombre", usuario.getPreNombre(),
                        "apellidoPaterno", usuario.getApellidoPaterno(),
                        "apellidoMaterno", usuario.getApellidoMaterno()
                ));
            }
    
            return ResponseEntity.badRequest().body("Correo o contraseña incorrectos");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al iniciar sesión: " + e.getMessage());
        }
    }
    
    @PutMapping("/update/{userId}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable int userId, @RequestBody ActualizacionDatosDTO datosDTO) {
        try {
            Optional<UsuarioModel> user = userService.obtenerUsuarioPorId(userId);
            if (user.isPresent()) {
                UsuarioModel usuario = user.get();

                usuario.setNombre(datosDTO.getNombre());
                usuario.setApellidoPaterno(datosDTO.getApellidoPaterno());
                usuario.setApellidoMaterno(datosDTO.getApellidoMaterno());
                usuario.setCorreoElectronico(datosDTO.getCorreoElectronico());
                usuario.setTelefono(datosDTO.getTelefono());
                usuario.setDni(datosDTO.getDni());
                usuario.setPreNombre(datosDTO.getPreNombre());

                if (datosDTO.getContraseña() != null && !datosDTO.getContraseña().isEmpty()) {
                    userService.actualizarContrasena(usuario, datosDTO.getContraseña());
                }

                userService.actualizarUsuario(usuario);
                return ResponseEntity.ok("Datos del usuario actualizados exitosamente");
            }
            return ResponseEntity.status(404).body("Usuario no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar los datos del usuario");
        }
    }

    @GetMapping("/user/{idUsuario}")
    public ResponseEntity<?> getUserById(@PathVariable int idUsuario) {
        try {
            Optional<UsuarioModel> user = userService.obtenerUsuarioPorId(idUsuario);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            }
            return ResponseEntity.status(404).body("Usuario no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener los datos del usuario");
        }
    }
}
