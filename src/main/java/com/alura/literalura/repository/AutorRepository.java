package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;


public interface AutorRepository extends JpaRepository<Autor,Long> {

    List<Autor> findAll();


    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :anioBuscado AND a.fechaFallecimiento >= :anioBuscado")
    List<Autor> listaAutorVivioDuranteEseAno(@Param("anioBuscado") Integer anioBuscado);
}