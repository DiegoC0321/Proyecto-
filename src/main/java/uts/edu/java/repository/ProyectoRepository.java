package uts.edu.java.repository;

import uts.edu.java.entity.Proyecto;
import uts.edu.java.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {
    List<Proyecto> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario);
}