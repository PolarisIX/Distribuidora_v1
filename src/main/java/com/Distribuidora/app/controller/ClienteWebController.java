package com.Distribuidora.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.Distribuidora.app.model.Cliente;
import com.Distribuidora.app.repository.ClienteRepository;
import com.Distribuidora.app.repository.ArticuloRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/clientes")
public class ClienteWebController {
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ArticuloRepository articuloRepository;

    @GetMapping
    public String listarClientes(@RequestParam(value = "accion", required = false) String accion, Model model) {
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("accion", accion); 
        return "clientes";
    }

    @GetMapping("/nuevo")
    public String nuevoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());

        // Obtener la lista de nombres de artículos y agregarla al modelo
        List<String> nombresArticulos = articuloRepository.findAll()
                .stream()
                .map(articulo -> articulo.getNombre())
                .collect(Collectors.toList());
        model.addAttribute("nombresArticulos", nombresArticulos);

        return "cliente_form";
    }

    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente) {
        clienteRepository.save(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable String id, Model model) {
        Cliente cliente = clienteRepository.findById(id).orElse(null);
        model.addAttribute("cliente", cliente);

        // Obtener la lista de nombres de artículos
        List<String> nombresArticulos = articuloRepository.findAll()
                .stream()
                .map(articulo -> articulo.getNombre())
                .collect(Collectors.toList());
        model.addAttribute("nombresArticulos", nombresArticulos);

        return "cliente_form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCliente(@PathVariable String id, @Valid @ModelAttribute Cliente cliente, BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<String> nombresArticulos = articuloRepository.findAll()
                    .stream()
                    .map(articulo -> articulo.getNombre())
                    .collect(Collectors.toList());
            model.addAttribute("nombresArticulos", nombresArticulos);
            return "cliente_form";
        }
        cliente.setId(id);
        clienteRepository.save(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable String id) {
        clienteRepository.deleteById(id);
        return "redirect:/clientes";
    }

    @PostMapping("/guardarNota/{id}")
    public String guardarNota(@PathVariable String id, @RequestParam("nota") String nota) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        cliente.setNota(nota);
        clienteRepository.save(cliente);
        return "redirect:/indexmecanico";
    }

    @GetMapping("/buscar")
    public String buscarCliente(@RequestParam("nombre") String nombre, Model model) {
        Cliente cliente = clienteRepository.findByNombre(nombre);
        if (cliente != null) {
            model.addAttribute("cliente", cliente);
            return "cliente_detalle";
        } else {
            model.addAttribute("mensaje", "Cliente no encontrado");
            return "clientes";
        }
    }
}

