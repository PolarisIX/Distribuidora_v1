package com.Distribuidora.app.controller;

import com.Distribuidora.app.model.Vendedor;
import com.Distribuidora.app.repository.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vendedores")
public class VendedorRestController {

    @Autowired
    private VendedorRepository vendedorRepo;

    // Obtener todos los vendedores
    @GetMapping
    public List<Vendedor> getAllVendedores() {
        return vendedorRepo.findAll();
    }

    // Obtener vendedor por ID
    @GetMapping("/{id}")
    public ResponseEntity<Vendedor> getVendedorById(@PathVariable String id) {
        Optional<Vendedor> vendedor = vendedorRepo.findById(id);
        return vendedor.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    // Crear nuevo vendedor
    @PostMapping
    public Vendedor createVendedor(@RequestBody Vendedor vendedor) {
        return vendedorRepo.save(vendedor);
    }

    // Actualizar vendedor existente
    @PutMapping("/{id}")
    public ResponseEntity<Vendedor> updateVendedor(@PathVariable String id, @RequestBody Vendedor vendedor) {
        Optional<Vendedor> optional = vendedorRepo.findById(id);
        if (optional.isPresent()) {
            vendedor.setId(id);
            return ResponseEntity.ok(vendedorRepo.save(vendedor));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar vendedor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendedor(@PathVariable String id) {
        if (vendedorRepo.existsById(id)) {
            vendedorRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

