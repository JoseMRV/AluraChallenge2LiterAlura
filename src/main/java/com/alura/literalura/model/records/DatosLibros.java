package com.alura.literalura.model.records;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibros(

        @JsonAlias("id") Long libroId,
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DatosAutor> datosAutor,
        @JsonAlias("languages") List<String> idioma,
        @JsonAlias("download_count") Long numeroDeDescargas
) {
}
