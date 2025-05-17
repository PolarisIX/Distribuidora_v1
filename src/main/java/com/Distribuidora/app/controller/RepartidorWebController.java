package com.Distribuidora.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.Distribuidora.app.model.Repartidor;
import com.Distribuidora.app.repository.RepartidorRepository;

@Controller
@RequestMapping("/repartidores")
public class RepartidorWebController {

    @Autowired
    private RepartidorRepository repartidorRepository;

    // Mostrar el formulario para agregar un repartidor
    @GetMapping("/nuevo")
    public String mostrarFormularioDeCreacion(Model model) {
        model.addAttribute("repartidor", new Repartidor());
        return "repartidores/formulario";  // Nombre de la vista HTML (Thymeleaf)
    }

    // Guardar un nuevo repartidor
    @PostMapping("/guardar")
    public String guardarRepartidor(@ModelAttribute Repartidor repartidor) {
        repartidorRepository.save(repartidor);
        return "redirect:/repartidores/listar";  // Redirigir a la lista de repartidores
    }

    // Ver detalles de un repartidor por correo
    @GetMapping("/ver/{correo}")
    public String verRepartidor(@PathVariable String correo, Model model) {
        Repartidor repartidor = repartidorRepository.findByCorreo(correo);
        if (repartidor != null) {
            model.addAttribute("repartidor", repartidor);
            return "repartidores/detalles";  // Vista que muestra los detalles
        }
        return "repartidores/no-encontrado";  // Vista si no se encuentra el repartidor
    }

    // Ver detalles de un repartidor por cédula
    @GetMapping("/ver-cedula/{cedula}")
    public String verRepartidorPorCedula(@PathVariable String cedula, Model model) {
        Repartidor repartidor = repartidorRepository.findByCedula(cedula);
        if (repartidor != null) {
            model.addAttribute("repartidor", repartidor);
            return "repartidores/detalles";  // Vista de detalles
        }
        return "repartidores/no-encontrado";  // Vista si no se encuentra
    }

    // Listar todos los repartidores
    @GetMapping("/listar")
    public String listarRepartidores(Model model) {
        model.addAttribute("repartidores", repartidorRepository.findAll());
        return "repartidores/lista";  // Vista que muestra la lista de repartidores
    }
}

