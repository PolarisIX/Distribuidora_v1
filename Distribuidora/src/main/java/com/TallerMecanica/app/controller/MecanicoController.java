package com.TallerMecanica.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.TallerMecanica.app.model.Cliente;
import com.TallerMecanica.app.repository.ClienteRepository;

@Controller
public class MecanicoController {

    @Autowired
    private ClienteRepository clienteRepository; // Accede directamente al repositorio

    @GetMapping("/indexmecanico")
    public String mostrarClientes(Model model) {
        // Obtén todos los clientes directamente desde el repositorio
        List<Cliente> clientes = clienteRepository.findAll(); 
        model.addAttribute("clientes", clientes);
        return "indexmecanico"; // El nombre de tu archivo HTML
    }

}
