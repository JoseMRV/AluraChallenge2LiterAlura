package com.alura.literalura.model;

import com.alura.literalura.model.records.DatosAutor;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;
    private Integer fechaNacimiento;
    private Integer fechaFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libros> libros;

    public Long getId(){
        return id;
    }
    public void setId(Long id){this.id = id;}

    public String getNombre(){return nombre;}
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaNacimiento(){
        return fechaNacimiento;
    }
    public void setFechaNacimiento(Integer fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getFechaFallecimiento(){
        return fechaFallecimiento;
    }
    public void setFechaFallecimiento(Integer fechaFallecimiento) {this.fechaFallecimiento = fechaFallecimiento;}

    public List<Libros> getLibros(){return libros;}

    public void setLibros(List<Libros> libros) {this.libros = libros;}

    public Autor(){}

    public Autor(DatosAutor autor){
        this.nombre = autor.nombre();
        this.fechaNacimiento = autor.fechaNacimiento();
        this.fechaFallecimiento = autor.fechaFallecimiento();
    }
    public Autor(String nombreAutor, Integer fechaDeNacimiento, Integer fechaDeFallecimiento) {
        this.nombre = nombreAutor;
        this.fechaNacimiento = fechaDeNacimiento;
        this.fechaFallecimiento = fechaDeFallecimiento;
    }
    @Override
    public String toString(){
        return
                "Autor: " + nombre + '\'' +
                        ", Fecha de Nacimiento: " + fechaNacimiento +
                        ", Fecha de Fallecimiento: " + fechaFallecimiento;
    }
}
