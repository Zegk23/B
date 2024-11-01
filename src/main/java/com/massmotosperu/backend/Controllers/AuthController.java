package com.massmotosperu.backend.Controllers;

import com.massmotosperu.backend.DTOs.LoginDTO;
import com.massmotosperu.backend.Models.UsuarioModel;
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

    @PostMapping("/register")
    public ResponseEntity<String> registrar(@RequestBody UsuarioModel usuario) {
        System.out.println("Datos recibidos: " + usuario.toString());

        if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre es obligatorio");
        }

        try {
            userService.registrarUsuario(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al registrar el usuario: " + e.getMessage());
        }

        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            Optional<UsuarioModel> user = userService.verificarUsuario(loginDTO.getCorreoElectronico(), loginDTO.getContraseña());
            if (user.isPresent()) {
                String token = jwtUtil.generarToken(user.get().getCorreoElectronico());
    
                // Crear un Map con el token y detalles del usuario
                Map<String, Object> response = Map.of(
                        "token", token,
                        "userId", user.get().getIdUsuario(), // Ahora devolvemos el userId nuevamente
                        "nombre", user.get().getNombre(),
                        "apellidoPaterno", user.get().getApellidoPaterno(),
                        "apellidoMaterno", user.get().getApellidoMaterno(),
                        "correoElectronico", user.get().getCorreoElectronico(),
                        "telefono", user.get().getTelefono(),
                        "dni", user.get().getDni()
                );
    
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body("Correo o contraseña incorrectos");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al iniciar sesión");
        }
    }
    

    @PutMapping("/update")
    public ResponseEntity<?> actualizarUsuario(@RequestBody UsuarioModel updatedUser,
                                               @RequestHeader("Authorization") String token) {
        try {
            // Extraer el correo del token JWT usando getSubject en lugar de getCorreoFromToken
            String correo = jwtUtil.getSubject(token.replace("Bearer ", ""));

            Optional<UsuarioModel> user = userService.obtenerUsuarioPorCorreo(correo);
            if (user.isPresent()) {
                UsuarioModel usuario = user.get();
                // Actualizar los datos básicos
                usuario.setNombre(updatedUser.getNombre());
                usuario.setApellidoPaterno(updatedUser.getApellidoPaterno());
                usuario.setApellidoMaterno(updatedUser.getApellidoMaterno());
                usuario.setCorreoElectronico(updatedUser.getCorreoElectronico());
                usuario.setTelefono(updatedUser.getTelefono());
                usuario.setDni(updatedUser.getDni());

                // Si se envía una nueva contraseña, la actualizamos
                if (updatedUser.getContraseña() != null && !updatedUser.getContraseña().isEmpty()) {
                    userService.actualizarContrasena(usuario, updatedUser.getContraseña());
                }

                userService.actualizarUsuario(usuario);
                return ResponseEntity.ok("Datos del usuario actualizados exitosamente");
            }
            return ResponseEntity.status(404).body("Usuario no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar los datos del usuario");
        }
    }
}
