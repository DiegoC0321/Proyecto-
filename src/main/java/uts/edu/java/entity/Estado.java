package uts.edu.java.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "estado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_estado")
    private String nombreEstado;

    @Column(name = "orden_columna")
    private Integer ordenColumna;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombreEstado() { return nombreEstado; }
    public void setNombreEstado(String nombreEstado) { this.nombreEstado = nombreEstado; }
    public Integer getOrdenColumna() { return ordenColumna; }
    public void setOrdenColumna(Integer ordenColumna) { this.ordenColumna = ordenColumna; }
}