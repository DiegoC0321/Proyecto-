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
@RequestMapping("/tareas")
public class TareaController {

    @Autowired private TareaRepository tareaRepo;
    @Autowired private ProyectoRepository proyectoRepo;
    @Autowired private EstadoRepository estadoRepo;
    @Autowired private EtiquetaRepository etiquetaRepo;
    @Autowired private UsuarioRepository usuarioRepo;

    @GetMapping("/proyecto/{proyectoId}")
    public String listar(@PathVariable Integer proyectoId, Model model, Principal principal) {
        Usuario usuario = usuarioRepo.findByCorreo(principal.getName()).orElse(null);
        Proyecto proyecto = proyectoRepo.findById(proyectoId).orElse(null);
        List<Tarea> tareas = tareaRepo.findByProyecto(proyecto);
        List<Estado> estados = estadoRepo.findAllByOrderByOrdenColumnaAsc();
        model.addAttribute("proyecto", proyecto);
        model.addAttribute("tareas", tareas);
        model.addAttribute("estados", estados);
        model.addAttribute("usuarioConectado", usuario);
        return "tareas/kanban";
    }

    @GetMapping("/nueva/{proyectoId}")
    public String nueva(@PathVariable Integer proyectoId, Model model, Principal principal) {
        Usuario usuario = usuarioRepo.findByCorreo(principal.getName()).orElse(null);
        Proyecto proyecto = proyectoRepo.findById(proyectoId).orElse(null);
        List<Estado> estados = estadoRepo.findAllByOrderByOrdenColumnaAsc();
        List<Etiqueta> etiquetas = etiquetaRepo.findAll();
        model.addAttribute("tarea", new Tarea());
        model.addAttribute("proyecto", proyecto);
        model.addAttribute("estados", estados);
        model.addAttribute("etiquetas", etiquetas);
        model.addAttribute("usuarioConectado", usuario);
        return "tareas/formulario";
    }

    @PostMapping("/guardar/{proyectoId}")
    public String guardar(@PathVariable Integer proyectoId,
                          @ModelAttribute Tarea tarea,
                          @RequestParam(required = false) List<Integer> etiquetaIds) {
        Proyecto proyecto = proyectoRepo.findById(proyectoId).orElse(null);
        tarea.setProyecto(proyecto);
        if (etiquetaIds != null) {
            etiquetaIds.forEach(eid -> {
                etiquetaRepo.findById(eid).ifPresent(e -> tarea.getEtiquetas().add(e));
            });
        }
        tareaRepo.save(tarea);
        return "redirect:/tareas/proyecto/" + proyectoId;
    }

    @PostMapping("/eliminar/{id}/{proyectoId}")
    public String eliminar(@PathVariable Integer id, @PathVariable Integer proyectoId) {
        tareaRepo.deleteById(id);
        return "redirect:/tareas/proyecto/" + proyectoId;
    }

    @PostMapping("/estado/{id}")
    public String cambiarEstado(@PathVariable Integer id,
                                @RequestParam Integer estadoId,
                                @RequestParam Integer proyectoId) {
        tareaRepo.findById(id).ifPresent(t -> {
            estadoRepo.findById(estadoId).ifPresent(t::setEstado);
            tareaRepo.save(t);
        });
        return "redirect:/tareas/proyecto/" + proyectoId;
    }
}