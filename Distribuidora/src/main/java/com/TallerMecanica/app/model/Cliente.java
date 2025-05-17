package com.TallerMecanica.app.model;

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

    private String cedula;

    @NotEmpty(message = "El nombre es obligatorio")
    private String nombre;

    @Pattern(regexp = "^(\\+\\d{1,2}\\s)?\\d{10}$", message = "El teléfono debe tener 10 dígitos")
    private String telefono;

    @Email(message = "Correo electrónico no válido")
    private String email;

    private String direccion;
    
    private List<Vehiculo> vehiculos; 
    
    private String Nota;
  
    private LocalDateTime fechaCreacion;

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

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public List<Vehiculo> getVehiculos() {
        return vehiculos;	
}
	 public void setVehiculos(List<Vehiculo> vehiculos) {
	        this.vehiculos = vehiculos;
}

	public String getNota() {
		return Nota;
	}

	public void setNota(String nota) {
		Nota = nota;
	}
	
	public Cliente() {
        // Inicializar la fecha de creación cuando el objeto Cliente se crea
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getter y Setter
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
	 
}