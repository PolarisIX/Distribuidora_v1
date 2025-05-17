package com.TallerMecanica.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.TallerMecanica.app.model.Vehiculo;
import com.TallerMecanica.app.repository.VehiculoRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/vehiculos")
public class VehiculoWebController {
	 @Autowired
	    private VehiculoRepository vehiculoRepository;

    @GetMapping
    public String listarVehiculos(Model model) {
        model.addAttribute("vehiculos", vehiculoRepository.findAll());
        return "vehiculos";
    }

    @GetMapping("/nuevo")
    public String nuevaVehiculo(Model model) {
        model.addAttribute("vehiculo", new Vehiculo());
        return "vehiculo_form";
    }


    @PostMapping("/guardar")
    public String guardarVehiculo(@ModelAttribute Vehiculo vehiculo) {
    
        vehiculoRepository.save(vehiculo);
        return "redirect:/vehiculos";
    }

    @GetMapping("/editar/{id}")
    public String editarVehiculo(@PathVariable("id") String id, Model model) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado: " + id));
        model.addAttribute("vehiculo", vehiculo);
        return "vehiculo_form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarVehiculo(@PathVariable String id, @Valid @ModelAttribute Vehiculo vehiculo, BindingResult result) {
        if (result.hasErrors()) {
            return "vehiculo_form";
        }
        vehiculo.setId(id);
        vehiculoRepository.save(vehiculo);
        return "redirect:/vehiculos";

    }
    @GetMapping("/eliminar/{id}")
    public String eliminarVehiculo(@PathVariable("id") String id, Model model) {
        try {
            Vehiculo vehiculo = vehiculoRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado: " + id));
            vehiculoRepository.delete(vehiculo);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "error"; // Redirigir a una página de error
        }
        return "redirect:/vehiculos";
    }
@GetMapping("/buscar")
public String buscarPlaca(@RequestParam("placa") String placa, Model model) {
	Vehiculo vehiculo = vehiculoRepository.findByPlaca(placa);
	
	if (vehiculo != null) {
		model.addAttribute("vehiculo", vehiculo);
		return "vehiculo_detalle";
	}else {
		model.addAttribute("mensaje", "Cliente no encontrado");
		return "vehiculo";
	}
	
}

}

