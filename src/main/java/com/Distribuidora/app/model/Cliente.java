package com.Distribuidora.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Document(collection = "clientes")
public class Cliente {

    @Id
    private String id;

    private String cedula; // Este campo ahora puede ser null

    @NotEmpty(message = "El nombre es obligatorio")
    private String nombre; // Este campo sigue siendo obligatorio

    @Pattern(regexp = "^(\\+\\d{1,2}\\s)?\\d{10}$", message = "El teléfono debe tener 10 dígitos")
    private String telefono; // Este campo puede ser null

    @Email(message = "Correo electrónico no válido")
    private String email; // Este campo puede ser null

    private String contra; // Campo para contraseña, sigue siendo obligatorio

    private String direccion; // Este campo puede ser null

    private List<Articulo> articulos; // Este campo puede ser null

    private String nota; // Este campo puede ser null

    private LocalDateTime fechaCreacion; // Este campo siempre tiene un valor por defecto

    public Cliente() {
        this.fechaCreacion = LocalDateTime.now(); // Asignación automática de la fecha de creación
    }

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Articulo> getArticulos() {
        return articulos;
    }

    public void setArticulos(List<Articulo> articulos) {
        this.articulos = articulos;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaCreacionFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return fechaCreacion.format(formatter);
    }
}

