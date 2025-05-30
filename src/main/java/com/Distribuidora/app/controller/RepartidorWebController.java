package com.Distribuidora.app.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.Distribuidora.app.model.Pedido;
import com.Distribuidora.app.model.Repartidor;
import com.Distribuidora.app.repository.PedidoRepository;
import com.Distribuidora.app.repository.RepartidorRepository;
import java.util.Optional;


@Controller
@RequestMapping("/repartidores")
public class RepartidorWebController {
	@Autowired
	private PedidoRepository pedidoRepository;


    @Autowired
    private RepartidorRepository repartidorRepository;
   

    @GetMapping
    public String listarRepartidoresRaiz(Model model) {
        model.addAttribute("repartidores", repartidorRepository.findAll());
        return "repartidores";  // Reutiliza la misma vista si existe
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeCreacion(Model model) {
        model.addAttribute("repartidor", new Repartidor());
        return "repartidor_form";
    }

    @PostMapping("/guardar")
    public String guardarRepartidor(@ModelAttribute Repartidor repartidor) {
        repartidorRepository.save(repartidor);
        return "redirect:/repartidores";  // Corregido
    }

    @GetMapping("/ver/{correo}")
    public String verRepartidor(@PathVariable String correo, Model model) {
        Repartidor repartidor = repartidorRepository.findByCorreo(correo);
        if (repartidor != null) {
            model.addAttribute("repartidor", repartidor);
            return "repartidores/detalles";
        }
        return "repartidores/no-encontrado";
    }

    @GetMapping("/ver-cedula/{cedula}")
    public String verRepartidorPorCedula(@PathVariable String cedula, Model model) {
        Repartidor repartidor = repartidorRepository.findByCedula(cedula);
        if (repartidor != null) {
            model.addAttribute("repartidor", repartidor);
            return "repartidores/detalles";
        }
        return "repartidores/no-encontrado";
    }

    @GetMapping("/listar")
    public String listarRepartidores(Model model) {
        model.addAttribute("repartidores", repartidorRepository.findAll());
        return "redirect:/repartidores";
    }

    // ✅ Método para eliminar un repartidor por ID
    @GetMapping("/eliminar/{id}")
    public String eliminarRepartidor(@PathVariable String id) {
        repartidorRepository.deleteById(id);
        return "redirect:/repartidores";
    }

    // Método GET para mostrar formulario de edición con datos existentes
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable String id, Model model) {
        Repartidor repartidor = repartidorRepository.findById(id).orElse(null);
        if (repartidor == null) {
            return "repartidores/no-encontrado";
        }
        model.addAttribute("repartidor", repartidor);
        return "repartidor_form"; // Reutiliza el formulario de creación, pero con datos cargados
    }

    // Método POST para actualizar
    @PostMapping("/editar/{id}")
    public String actualizarRepartidor(@PathVariable String id, @ModelAttribute Repartidor repartidor) {
        Repartidor existente = repartidorRepository.findById(id).orElse(null);
        if (existente != null) {
            existente.setCedula(repartidor.getCedula());
            existente.setNombre(repartidor.getNombre());
            existente.setCorreo(repartidor.getCorreo());
            existente.setContrasena(repartidor.getContrasena());
            existente.setTelefono(repartidor.getTelefono());
            existente.setDireccion(repartidor.getDireccion());
            existente.setEstado(repartidor.getEstado());
            existente.setDisponibilidad(repartidor.getDisponibilidad());

            repartidorRepository.save(existente);
            return "redirect:/repartidores";
        }
        return "repartidores/no-encontrado";
    }
    
    @GetMapping("/pedidos")
    public String verTodosLosPedidos(Model model) {
        List<Pedido> pedidos = pedidoRepository.findAll();  // Trae todos los pedidos
        model.addAttribute("pedidos", pedidos);
        return "indexRepartidor";  // nombre de la plantilla Thymeleaf que mostraste
    }
    @GetMapping("/gestionPedidos")
    public String verLosPedidos(Model model) {
        List<Pedido> pedidos = pedidoRepository.findAll();  // Trae todos los pedidos
        model.addAttribute("pedidos", pedidos);
        return "gestionPedidos";  
    }
}