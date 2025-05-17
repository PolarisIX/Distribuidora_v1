package com.Distribuidora.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@Document(collection = "bodega")
public class Bodega {

    @Id
    private String id;

    private String cedula;

    @NotEmpty(message = "El nombre es obligatorio")
    private String nombre;

    @Email(message = "Correo electrónico no válido")
    @NotEmpty(message = "El correo es obligatorio")
    private String email;

    @NotEmpty(message = "La contraseña es obligatoria")
    private String contra;

    @Pattern(regexp = "^(\\+\\d{1,2}\\s)?\\d{10}$", message = "El teléfono debe tener 10 dígitos")
    private String telefono;

    private String direccion;

    private String turno; // Ej: "Mañana", "Tarde", "Noche"

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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }
}
