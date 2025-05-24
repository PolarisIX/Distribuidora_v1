package com.Distribuidora.app.controller;

import com.Distribuidora.app.model.Pedido;
import com.Distribuidora.app.model.Cliente;
import com.Distribuidora.app.model.Articulo;
import com.Distribuidora.app.repository.PedidoRepository;
import com.Distribuidora.app.repository.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoRestController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Obtener todos los pedidos
    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    // Obtener pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedido(@PathVariable String id) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        return pedidoOpt.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo pedido
    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido) {
        // Validar que el cliente exista
        if (pedido.getClienteId() == null || !clienteRepository.existsById(pedido.getClienteId())) {
            return ResponseEntity.badRequest().body("Cliente no válido o no encontrado");
        }

        // Calcular total del pedido
        List<Articulo> articulos = pedido.getArticulos();
        if (articulos == null || articulos.isEmpty()) {
            return ResponseEntity.badRequest().body("El pedido debe tener al menos un artículo");
        }

        double total = articulos.stream()
            .mapToDouble(a -> a.getCantidad() * a.getPrecio())
            .sum();
        pedido.setTotal(total);

        pedido.setFechaPedido(java.time.LocalDateTime.now());
        pedido.setEstado("pendiente");

        Pedido guardado = pedidoRepository.save(pedido);
        return ResponseEntity.ok(guardado);
    }

    // Actualizar un pedido existente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPedido(@PathVariable String id, @RequestBody Pedido pedidoActualizado) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pedido pedido = pedidoOpt.get();
        pedido.setArticulos(pedidoActualizado.getArticulos());
        pedido.setDireccionEntrega(pedidoActualizado.getDireccionEntrega());
        pedido.setNotas(pedidoActualizado.getNotas());
        pedido.setEstado(pedidoActualizado.getEstado());

        double total = pedido.getArticulos().stream()
            .mapToDouble(a -> a.getCantidad() * a.getPrecio())
            .sum();
        pedido.setTotal(total);

        return ResponseEntity.ok(pedidoRepository.save(pedido));
    }

    // Eliminar un pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPedido(@PathVariable String id) {
        if (!pedidoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        pedidoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

