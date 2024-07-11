package com.alura.literalura.model;

import com.alura.literalura.model.records.DatosLibros;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long libroId;

    @Column(unique = true)
    private String titulo;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "autor_id")
    private Autor autor;
    private  String idioma;
    private Long numeroDeDescargas;

    public Libros(){}

    public Libros(DatosLibros datosLibros){
        this.libroId = datosLibros.libroId();
        this.titulo = datosLibros.titulo();
        if (datosLibros.datosAutor() != null && !datosLibros.datosAutor().isEmpty()){
            this.autor = new Autor(datosLibros.datosAutor().get(0));
        }else {
            this.autor = null;
        }
        this.numeroDeDescargas = datosLibros.numeroDeDescargas();
        this.idioma = idiomaDesconocido(datosLibros.idioma());

    }
    private String idiomaDesconocido(List<String> idiomas){
        if (idiomas == null || idiomas.isEmpty()){
            return "Desconocido";
        }
        return idiomas.get(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLibroId() {
        return libroId;
    }

    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Long getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Long numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString(){
        return
                "  \nid=" + id +
                        "  \nLibro id: " + libroId +
                        ", \nTítulo: " + titulo + '\'' +
                        ", \nAutor: " + (autor != null ? autor.getNombre() : "N/A")+
                        ", \nIdioma: " + idioma +
                        ", \nNumero de descargas: " + numeroDeDescargas;
    }
}

