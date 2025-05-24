package com.Distribuidora.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Document(collection = "pedidos")
public class Pedido {

    @Id
    private String id;

    @NotNull(message = "El ID del cliente es obligatorio")
    private String clienteId; // Solo se guarda el ID del cliente

    @NotNull(message = "La lista de artículos no puede estar vacía")
    private List<Articulo> articulos;

    private Repartidor repartidor; // Asignado cuando esté disponible

    private LocalDateTime fechaPedido;

    private String estado; // Ej: "pendiente", "en camino", "entregado", "cancelado"

    private double total;

    private String direccionEntrega;

    private String notas; // Notas del cliente u observaciones adicionales

    public Pedido() {
        this.fechaPedido = LocalDateTime.now();
        this.estado = "pendiente";
    }

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public List<Articulo> getArticulos() {
        return articulos;
    }

    public void setArticulos(List<Articulo> articulos) {
        this.articulos = articulos;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(Repartidor repartidor) {
        this.repartidor = repartidor;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getFechaPedidoFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fechaPedido.format(formatter);
    }
}
