package com.Distribuidora.app.repository;

import com.Distribuidora.app.model.Pedido;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface PedidoRepository extends MongoRepository<Pedido, String> {
	List<Pedido> findByClienteId(String clienteId);

}
 