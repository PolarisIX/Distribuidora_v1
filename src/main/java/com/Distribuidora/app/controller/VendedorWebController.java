package com.Distribuidora.app.controller;

import com.Distribuidora.app.model.Vendedor;
import com.Distribuidora.app.repository.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vendedores")
public class VendedorWebController {

    @Autowired
    private VendedorRepository vendedorRepo;

    // Mostrar todos los vendedores
    @GetMapping
    public String verVendedores(Model model) {
        model.addAttribute("listaVendedores", vendedorRepo.findAll());
        return "verVendedores";
    }

    // Mostrar formulario de nuevo vendedor
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("vendedor", new Vendedor());
        return "Vendedor_form";
    }

    // Guardar o actualizar vendedor
    @PostMapping("/guardar")
    public String guardarVendedor(@ModelAttribute Vendedor vendedor) {
        vendedorRepo.save(vendedor);
        return "redirect:/vendedores";
    }

    // Mostrar formulario para editar vendedor existente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable String id, Model model) {
        Vendedor vendedor = vendedorRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("ID no válido: " + id));
        model.addAttribute("vendedor", vendedor);
        return "Vendedor_form";
    }

    // Eliminar vendedor
    @GetMapping("/eliminar/{id}")
    public String eliminarVendedor(@PathVariable String id) {
        vendedorRepo.deleteById(id);
        return "redirect:/vendedores";
    }
    
    @PostMapping("/actualizar/{id}")
    public String actualizarVendedor(@PathVariable String id, @ModelAttribute Vendedor vendedor) {
        Vendedor vendedorExistente = vendedorRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID no válido: " + id));

        vendedorExistente.setNombre(vendedor.getNombre());
        vendedorExistente.setApellido(vendedor.getApellido());
        vendedorExistente.setCedula(vendedor.getCedula());
        vendedorExistente.setEmail(vendedor.getEmail());
        vendedorExistente.setTelefono(vendedor.getTelefono());
        vendedorExistente.setDireccionTienda(vendedor.getDireccionTienda());
        vendedorExistente.setNombreTienda(vendedor.getNombreTienda());
        vendedorExistente.setCategoriaProducto(vendedor.getCategoriaProducto());
        vendedorExistente.setContrasena(vendedor.getContrasena());

        vendedorRepo.save(vendedorExistente);

        return "redirect:/vendedores";
    }

}
