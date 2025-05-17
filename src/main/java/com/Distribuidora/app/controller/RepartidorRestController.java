package com.Distribuidora.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Distribuidora.app.model.Repartidor;
import com.Distribuidora.app.repository.RepartidorRepository;

@RestController
@RequestMapping("/api/repartidores")
public class RepartidorRestController {

    @Autowired
    private RepartidorRepository repartidorRepository;

    // Crear un nuevo repartidor
    @PostMapping
    public ResponseEntity<Repartidor> crearRepartidor(@RequestBody Repartidor repartidor) {
        Repartidor nuevoRepartidor = repartidorRepository.save(repartidor);
        return new ResponseEntity<>(nuevoRepartidor, HttpStatus.CREATED);
    }

    // Obtener repartidor por correo
    @GetMapping("/correo/{correo}")
    public ResponseEntity<Repartidor> obtenerRepartidorPorCorreo(@PathVariable String correo) {
        Repartidor repartidor = repartidorRepository.findByCorreo(correo);
        if (repartidor != null) {
            return new ResponseEntity<>(repartidor, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Obtener repartidor por cédula
    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<Repartidor> obtenerRepartidorPorCedula(@PathVariable String cedula) {
        Repartidor repartidor = repartidorRepository.findByCedula(cedula);
        if (repartidor != null) {
            return new ResponseEntity<>(repartidor, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Actualizar repartidor
    @PutMapping("/{id}")
    public ResponseEntity<Repartidor> actualizarRepartidor(@PathVariable String id, @RequestBody Repartidor repartidor) {
        if (repartidorRepository.existsById(id)) {
            repartidor.setId(id);
            Repartidor repartidorActualizado = repartidorRepository.save(repartidor);
            return new ResponseEntity<>(repartidorActualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Eliminar repartidor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRepartidor(@PathVariable String id) {
        if (repartidorRepository.existsById(id)) {
            repartidorRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}


