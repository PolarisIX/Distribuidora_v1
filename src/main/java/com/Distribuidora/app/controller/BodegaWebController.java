package com.Distribuidora.app.controller;

import com.Distribuidora.app.model.Bodega;
import com.Distribuidora.app.repository.BodegaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bodegas")
public class BodegaWebController {

    @Autowired
    private BodegaRepository bodegaRepo;

    // Listar todas las bodegas
    @GetMapping
    public String verBodegas(Model model) {
        model.addAttribute("listaBodegas", bodegaRepo.findAll());
        return "verBodega";
    }

    // Mostrar formulario de nueva bodega
    @GetMapping("/nuevo")
    public String mostrarFormularioNueva(Model model) {
        model.addAttribute("bodega", new Bodega());
        return "formBodega";
    }

    // Guardar nueva bodega
    @PostMapping("/guardar")
    public String guardarBodega(@ModelAttribute Bodega bodega) {
        bodegaRepo.save(bodega);
        return "redirect:/bodegas";
    }

    // Mostrar formulario para editar bodega existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable String id, Model model) {
        Bodega bodega = bodegaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no válido: " + id));
        model.addAttribute("bodega", bodega);
        return "formBodega";
    }

    // Actualizar bodega
    @PostMapping("/actualizar/{id}")
    public String actualizarBodega(@PathVariable String id, @ModelAttribute Bodega bodega) {
        Bodega bodegaExistente = bodegaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no válido: " + id));

        bodegaExistente.setCedula(bodega.getCedula());
        bodegaExistente.setNombre(bodega.getNombre());
        bodegaExistente.setEmail(bodega.getEmail());
        bodegaExistente.setContra(bodega.getContra());
        bodegaExistente.setTelefono(bodega.getTelefono());
        bodegaExistente.setDireccion(bodega.getDireccion());
        bodegaExistente.setTurno(bodega.getTurno());

        bodegaRepo.save(bodegaExistente);

        return "redirect:/bodegas";
    }

    // Eliminar bodega
    @GetMapping("/eliminar/{id}")
    public String eliminarBodega(@PathVariable String id) {
        bodegaRepo.deleteById(id);
        return "redirect:/bodegas";
    }
}
