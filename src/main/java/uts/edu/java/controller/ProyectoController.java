package uts.edu.java.controller;

import uts.edu.java.entity.*;
import uts.edu.java.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/proyectos")
public class ProyectoController {

    @Autowired private ProyectoRepository proyectoRepo;
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private TareaRepository tareaRepo;

    @GetMapping
    public String listar(Model model, Principal principal) {
        Usuario usuario = usuarioRepo.findByCorreo(principal.getName()).orElse(null);
        List<Proyecto> proyectos = proyectoRepo.findByUsuarioOrderByFechaCreacionDesc(usuario);

        // Mapa de progreso por proyecto: tareas finalizadas / total
        java.util.Map<Integer, int[]> progreso = new java.util.HashMap<>();
        for (Proyecto p : proyectos) {
            List<uts.edu.java.entity.Tarea> tareas = tareaRepo.findByProyecto(p);
            long finalizadas = tareas.stream()
                .filter(t -> t.getEstado().getNombreEstado().equalsIgnoreCase("Finalizado"))
                .count();
            progreso.put(p.getId(), new int[]{(int) finalizadas, tareas.size()});
        }

        model.addAttribute("proyectos", proyectos);
        model.addAttribute("progreso", progreso);
        model.addAttribute("usuarioConectado", usuario);
        return "proyectos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model, Principal principal) {
        Usuario usuario = usuarioRepo.findByCorreo(principal.getName()).orElse(null);
        model.addAttribute("proyecto", new Proyecto());
        model.addAttribute("usuarioConectado", usuario);
        return "proyectos/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Proyecto proyecto, Principal principal) {
        Usuario usuario = usuarioRepo.findByCorreo(principal.getName()).orElse(null);
        proyecto.setUsuario(usuario);
        proyectoRepo.save(proyecto);
        return "redirect:/proyectos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, Principal principal) {
        Usuario usuario = usuarioRepo.findByCorreo(principal.getName()).orElse(null);
        Proyecto proyecto = proyectoRepo.findById(id).orElse(null);
        model.addAttribute("proyecto", proyecto);
        model.addAttribute("usuarioConectado", usuario);
        return "proyectos/formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        proyectoRepo.deleteById(id);
        return "redirect:/proyectos";
    }
}