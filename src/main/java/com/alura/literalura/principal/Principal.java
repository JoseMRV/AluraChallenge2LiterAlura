package com.alura.literalura.principal;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libros;
import com.alura.literalura.model.records.Datos;
import com.alura.literalura.model.records.DatosLibros;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibrosRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);
    private LibrosRepository librosRepository;
    private AutorRepository autorRepository;
    public Principal(LibrosRepository librosRepository,AutorRepository autorRepository){
        this.librosRepository = librosRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu(){
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                   
                    Bienvenido
                    
                    1- Agrega un libro a la biblioteca.
                    2- Buscar libro por título en la biblioteca.
                    3- Libros en la biblioteca.
                    4- Buscar Autor por año en la biblioteca.
                    5- Autores registrados en la biblioteca.
                    6- Buscar libros por idioma en la biblioteca.
                    
                    0- Salir.
                    
                    """;
            try{
                System.out.println(menu);
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (InputMismatchException e){
                System.out.println("Opción inválida." +
                        "\nIntente de nuevo...");
                teclado.nextLine();
                continue;
            }

            switch (opcion){
                case 1:
                    buscarLibroEnLaAPI();
                    break;
                case 2:
                    buscarLibroPorTitulo();
                    break;
                case 3:
                    librosBuscados();
                    break;
                case 4:
                    buscarAutoresPorAnio();
                    break;
                case 5:
                    autoresBuscados();
                    break;
                case 6:
                    buscarLibroPorIdioma();
                    break;
                case 0:
                    opcion = 0;
                    System.out.println("\nHasta Luego!");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private Libros getDatosLibros(){
        System.out.println("Ingrese el nombre del libro: ");
        var nombreLibro = teclado.nextLine().toUpperCase();
        var json = consumoAPI.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "%20"));
        Datos datos = conversor.obtenerDatos(json, Datos.class);
        if (datos != null && datos.getResultadoLibros() != null && !datos.getResultadoLibros().isEmpty()){
            DatosLibros primerLibro = datos.getResultadoLibros().get(0);
            return new Libros(primerLibro);
        }else {
            System.out.println("No se encontró resultado.");
            return null;
        }
    }

    private void buscarLibroEnLaAPI(){
        Libros libros = getDatosLibros();
        if (libros == null){
            System.out.println("Libro NO encontrado.");
            return;
        }
        try{
            boolean libroRegistrado = librosRepository.existsByTitulo(libros.getTitulo());
            if (libroRegistrado){
                System.out.println("El libro ya se encuentra en la biblioteca.");
            }else {
                librosRepository.save(libros);
                System.out.println(libros.toString());
            }
        }catch (InvalidDataAccessApiUsageException e){
            System.out.println("Acción no valida.");
        }
    }

    private void buscarLibroPorTitulo(){
        System.out.println("Ingrese Titulo libro que quiere buscar: ");
        var titulo = teclado.nextLine();
        Libros libroBuscado = librosRepository.findByTituloContainsIgnoreCase(titulo);
        if (libroBuscado != null){
            System.out.println("El libro que buscó es: " + libroBuscado);
        }else {
            System.out.println("Lo siento, el libro con el titulo: " + titulo + " no se encontró.");
        }
    }

    @Transactional(readOnly = true)
    private void librosBuscados(){
        List<Libros> libros = librosRepository.findAll();
        if (libros.isEmpty()){
            System.out.println("No se encontraron libros en la biblioteca.");
        }else {
            System.out.println("Libros en la biblioteca: ");
            for (Libros libro : libros){
                System.out.println(libro.toString());
            }
        }
    }

    private void buscarAutoresPorAnio() {
        try {
            System.out.println("Indica el año para consultar: ");
            var anioBuscado = teclado.nextInt();
            List<Autor> autoresVivos = autorRepository.listaAutorVivioDuranteEseAno(anioBuscado);

            if (autoresVivos.isEmpty()) {
                System.out.println("No se encontraron autores que vivieran en el año: " + anioBuscado);
            } else {
                autoresVivos.forEach(autor -> {
                    System.out.println("\nNombre: " + autor.getNombre());
                    System.out.println("Fecha de Nacimiento: " + autor.getFechaNacimiento());
                    System.out.println("Fecha de Fallecimiento: " + (autor.getFechaFallecimiento() != null ? autor.getFechaFallecimiento() : "N/A"));
                    System.out.println("Libros: " + (autor.getLibros() != null ? autor.getLibros().stream().map(Libros::getTitulo).reduce((a, b) -> a + ", " + b).orElse("N/A") : "N/A"));
                    System.out.println();
                });
            }
        } finally {
        }
    }

    @Transactional
    private void autoresBuscados() {
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("No se encontraron Autores en la biblioteca.");
        } else {
            System.out.println("Autores encontrados en la biblioteca: \n");
            Set<String> autoresUnicos = new HashSet<>();
            for (Autor autor : autores) {
                if (autoresUnicos.add(autor.getNombre())) {
                    System.out.println("Nombre: " + autor.getNombre());
                    System.out.println("Fecha de Nacimiento: " + autor.getFechaNacimiento());
                    System.out.println("Fecha de Fallecimiento: " + (autor.getFechaFallecimiento() != null ? autor.getFechaFallecimiento() : "N/A"));
                    System.out.println();
                }
            }
        }
    }

    private void buscarLibroPorIdioma(){
        System.out.println("Ingrese el idioma en el que quiere buscar: \n");
        System.out.println("  Opción - es : Libros en español. \n");
        System.out.println("  Opción - en : Libros en ingles.  \n");

        var idioma = teclado.nextLine();
        List<Libros> librosPorIdioma = librosRepository.findByIdioma(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en la biblioteca.");
        } else {
            System.out.println("Estos son los libros en la biblioteca de acuerdo al idioma seleccionado:");
            for (Libros libros : librosPorIdioma) {
                System.out.println(libros.toString());
            }
        }
    }
}
