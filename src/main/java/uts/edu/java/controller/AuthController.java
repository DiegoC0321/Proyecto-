package uts.edu.java.controller;

import uts.edu.java.entity.*;
import uts.edu.java.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Controller
public class AuthController {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ProyectoRepository proyectoRepository;
    @Autowired private TareaRepository tareaRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home() { return "redirect:/login"; }

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
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        Usuario usuario = usuarioRepository.findByCorreo(principal.getName()).orElse(null);
        List<Proyecto> proyectos = proyectoRepository.findByUsuarioOrderByFechaCreacionDesc(usuario);
        List<Tarea> tareas = tareaRepository.findByProyectoIn(proyectos);
        long pendientes = tareas.stream().filter(t -> t.getEstado().getNombreEstado().equals("Pendiente")).count();
        long urgentes = tareas.stream().filter(t -> t.getFechaLimite() != null &&
            !t.getFechaLimite().isAfter(java.time.LocalDate.now().plusDays(3))).count();
        model.addAttribute("usuarioConectado", usuario);
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("tareas", tareas);
        model.addAttribute("totalProyectos", proyectos.size());
        model.addAttribute("tareasPendientes", pendientes);
        model.addAttribute("tareasUrgentes", urgentes);
        return "dashboard";
    }
}