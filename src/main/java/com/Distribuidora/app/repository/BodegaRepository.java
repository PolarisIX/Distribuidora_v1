package com.Distribuidora.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.Distribuidora.app.model.Bodega;

public interface BodegaRepository extends MongoRepository<Bodega, String> {
    Bodega findByCedula(String cedula);
}
