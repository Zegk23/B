package com.massmotosperu.backend.Controllers;

import com.massmotosperu.backend.DTOs.ActualizacionDatosDTO;
import com.massmotosperu.backend.DTOs.LoginDTO;
import com.massmotosperu.backend.Models.UsuarioModel;
import com.massmotosperu.backend.Services.UserService;
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

    @PostMapping("/register")
    public ResponseEntity<String> registrar(@RequestBody UsuarioModel usuario) {
        System.out.println("Datos recibidos: " + usuario);

        if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre es obligatorio");
        }

        try {
            userService.registrarUsuario(usuario);
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al registrar el usuario: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            Optional<UsuarioModel> user = userService.verificarUsuario(loginDTO.getCorreoElectronico(), loginDTO.getContraseña());
            if (user.isPresent()) {
                // Crear un Map con los detalles del usuario (sin token)
                Map<String, Object> response = Map.of(
                        "userId", user.get().getIdUsuario(),
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

    @PutMapping("/update/{userId}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable int userId, @RequestBody ActualizacionDatosDTO datosDTO) {
        try {
            Optional<UsuarioModel> user = userService.obtenerUsuarioPorId(userId);
            if (user.isPresent()) {
                UsuarioModel usuario = user.get();

                // Actualizar los datos del usuario usando el DTO
                usuario.setNombre(datosDTO.getNombre());
                usuario.setApellidoPaterno(datosDTO.getApellidoPaterno());
                usuario.setApellidoMaterno(datosDTO.getApellidoMaterno());
                usuario.setCorreoElectronico(datosDTO.getCorreoElectronico());
                usuario.setTelefono(datosDTO.getTelefono());
                usuario.setDni(datosDTO.getDni());
                usuario.setPreNombre(datosDTO.getPreNombre());

                // Si se envía una nueva contraseña, encripta y actualiza
                if (datosDTO.getContraseña() != null && !datosDTO.getContraseña().isEmpty()) {
                    userService.actualizarContrasena(usuario, datosDTO.getContraseña());
                }

                userService.actualizarUsuario(usuario); // Guarda los cambios en la base de datos
                return ResponseEntity.ok("Datos del usuario actualizados exitosamente");
            }
            return ResponseEntity.status(404).body("Usuario no encontrado");
        } catch (Exception e) {
            e.printStackTrace();
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
