package com.TallerMecanica.app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TallerMecanica.app.model.Vehiculo;
import com.TallerMecanica.app.repository.VehiculoRepository;

@RestController
@RequestMapping("/api/vehiculo")
public class VehiculoRestController {

    @Autowired
    private VehiculoRepository vehiculoRepo;

    // Crear un nuevo vehículo
    @PostMapping
    public ResponseEntity<?> createVehiculo(@RequestBody Vehiculo vehiculo) {
        try {
            Vehiculo newVehiculo = vehiculoRepo.save(vehiculo);
            return new ResponseEntity<Vehiculo>(newVehiculo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todos los vehículos
    @GetMapping
    public ResponseEntity<?> showAllVehiculos() {
        try {
            List<Vehiculo> listVehiculo = vehiculoRepo.findAll();
            return new ResponseEntity<List<Vehiculo>>(listVehiculo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un vehículo existente
    @PutMapping
    public ResponseEntity<?> updateVehiculo(@RequestBody Vehiculo vehiculo) {
        try {
            Optional<Vehiculo> vehiculoOptional = vehiculoRepo.findById(vehiculo.getId());
            if (vehiculoOptional.isPresent()) {
                Vehiculo updatedVehiculo = vehiculoRepo.save(vehiculo);
                return ResponseEntity.ok(updatedVehiculo);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehículo no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause().toString());
        }
    }

    // Eliminar un vehículo por ID
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteVehiculo(@PathVariable("id") String id) {
        try {
            vehiculoRepo.deleteById(id);
            return new ResponseEntity<String>("Vehículo eliminado", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

    /*
    // Método de login (si aplicable, para vehículos, puedes usar otro criterio de autenticación)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Vehiculo vehiculo) {
        Vehiculo existeVehiculo = vehiculoRepo.findVehiculoByPlaca(vehiculo.getPlaca());
        if (existeVehiculo != null && existeVehiculo.getContraseña().equals(vehiculo.getContraseña())) {
            return ResponseEntity.ok("Inicio de sesión exitoso");
        } else {
            return Response
*/