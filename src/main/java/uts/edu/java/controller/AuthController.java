package uts.edu.java.controller;

import uts.edu.java.entity.Usuario;
import uts.edu.java.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("error", "Correo o contraseña incorrectos.");
        if (logout != null) model.addAttribute("mensaje", "Sesión cerrada correctamente.");
        return "login";
    }

    @GetMapping("/registro")
    public String registroPage(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            model.addAttribute("error", "El correo ya está registrado.");
            return "registro";
        }
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        usuarioRepository.save(usuario);
        return "redirect:/login?registrado=true";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}