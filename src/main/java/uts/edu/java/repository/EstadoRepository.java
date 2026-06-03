package uts.edu.java.repository;

import uts.edu.java.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EstadoRepository extends JpaRepository<Estado, Integer> {
    List<Estado> findAllByOrderByOrdenColumnaAsc();
}