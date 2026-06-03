package uts.edu.java.repository;

import uts.edu.java.entity.Tarea;
import uts.edu.java.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea, Integer> {
    List<Tarea> findByProyecto(Proyecto proyecto);
    List<Tarea> findByProyectoIn(List<Proyecto> proyectos);
}