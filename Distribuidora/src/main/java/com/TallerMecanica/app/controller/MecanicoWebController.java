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

import com.TallerMecanica.app.model.Mecanico;
import com.TallerMecanica.app.repository.MecanicoRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/mecanicos")
public class MecanicoWebController {

    @Autowired
    private MecanicoRepository mecanicoRepository;

    // Mostrar la lista de mecánicos
    @GetMapping
    public String listarMecanicos(@RequestParam(value = "accion", required = false) String accion, Model model) {
        model.addAttribute("mecanicos", mecanicoRepository.findAll());
        model.addAttribute("accion", accion); // Pasar acción a la vista para mostrar mensajes
        return "mecanicos";
    }

    // Mostrar el formulario para agregar un nuevo mecánico
    @GetMapping("/nuevo")
    public String nuevoMecanico(Model model) {
        model.addAttribute("mecanico", new Mecanico());
        return "mecanico_form";
    }

    // Guardar un nuevo mecánico
    @PostMapping("/guardar")
    public String guardarMecanico(@Valid @ModelAttribute Mecanico mecanico, BindingResult result) {
        if (result.hasErrors()) {
            return "mecanico_form";
        }
        mecanicoRepository.save(mecanico);
        return "redirect:/mecanicos?accion=agregado"; // Redirigir al listado con mensaje de éxito
    }

    // Mostrar el formulario para editar un mecánico existente
    @GetMapping("/editar/{id}")
    public String editarMecanico(@PathVariable String id, Model model) {
        Mecanico mecanico = mecanicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mecánico no encontrado con id: " + id));
        model.addAttribute("mecanico", mecanico);
        return "mecanico_form";
    }

    // Actualizar un mecánico existente
    @PostMapping("/actualizar/{id}")
    public String actualizarMecanico(@PathVariable String id, @Valid @ModelAttribute Mecanico mecanico, BindingResult result) {
        if (result.hasErrors()) {
            return "mecanico_form";
        }
        mecanico.setId(id);
        mecanicoRepository.save(mecanico);
        return "redirect:/mecanicos?accion=editado"; // Redirigir al listado con mensaje de éxito
    }

    // Eliminar un mecánico
    @GetMapping("/eliminar/{id}")
    public String eliminarMecanico(@PathVariable String id) {
        mecanicoRepository.deleteById(id);
        return "redirect:/mecanicos?accion=eliminado";
    }
  
}
