package com.TallerMecanica.app.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotEmpty;
@Document(collection = "vehiculos")
public class Vehiculo {
    @Id
    private String id;

    @NotEmpty(message = "La marca es obligatoria")
    private String marca;

    @NotEmpty(message = "El modelo es obligatorio")
    private String modelo;

    private int anio;

    @NotEmpty(message = "La placa es obligatoria")
    private String placa;
    
    @NotEmpty(message = "El tipo es obligatorio")
    private String tipo;

    @NotEmpty(message = "El color es obligatorio")
    private String color;
    
    private LocalDateTime fechaCreacion;
    
    private String estado;

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Vehiculo() {
        // Inicializar la fecha de creación cuando el objeto Cliente se crea
        this.fechaCreacion = LocalDateTime.now();
    }
	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	 public String getFechaCreacionFormateada() {
	        // Formatear la fecha para mostrarla en un formato legible
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	        return fechaCreacion.format(formatter);
	    }

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	 
	 
	 
}
