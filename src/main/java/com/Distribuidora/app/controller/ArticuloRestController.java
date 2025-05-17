package com.Distribuidora.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Distribuidora.app.model.Articulo;
import com.Distribuidora.app.repository.ArticuloRepository;

@RestController
@RequestMapping("/api/articulo")
public class ArticuloRestController {

    @Autowired
    private ArticuloRepository articuloRepo;

    // Crear un nuevo artículo
    @PostMapping
    public ResponseEntity<?> createArticulo(@RequestBody Articulo articulo) {
        try {
            Articulo newArticulo = articuloRepo.save(articulo);
            return new ResponseEntity<>(newArticulo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todos los artículos
    @GetMapping
    public ResponseEntity<?> showAllArticulos() {
        try {
            List<Articulo> listArticulo = articuloRepo.findAll();
            return new ResponseEntity<>(listArticulo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener un artículo por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getArticuloById(@PathVariable String id) {
        Optional<Articulo> articulo = articuloRepo.findById(id);
        if (articulo.isPresent()) {
            return new ResponseEntity<>(articulo.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Artículo no encontrado", HttpStatus.NOT_FOUND);
        }
    }


    // Actualizar un artículo existente
    @PutMapping
    public ResponseEntity<?> updateArticulo(@RequestBody Articulo articulo) {
        try {
            Optional<Articulo> articuloOptional = articuloRepo.findById(articulo.getId());
            if (articuloOptional.isPresent()) {
                Articulo updatedArticulo = articuloRepo.save(articulo);
                return ResponseEntity.ok(updatedArticulo);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artículo no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause().toString());
        }
    }

    // Eliminar un artículo por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArticulo(@PathVariable String id) {
        try {
            articuloRepo.deleteById(id);
            return new ResponseEntity<>("Artículo eliminado", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
