package com.Distribuidora.app.controller;

import com.Distribuidora.app.model.Cliente;
import com.Distribuidora.app.model.Administrador;
import com.Distribuidora.app.repository.ClienteRepository;
import com.Distribuidora.app.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private ClienteRepository clienteRepo;

    @Autowired
    private AdministradorRepository administradorRepo;

    // Mostrar la página de login para Clientes
    @GetMapping("/cliente")
    public String mostrarLoginCliente() {
        return "login-cliente"; // Página de login para clientes
    }

    // Procesar el login de Cliente
    @PostMapping("/cliente")
    public String loginCliente(@ModelAttribute Cliente cliente, Model model) {
        Cliente clienteEncontrado = clienteRepo.findByCedula(cliente.getCedula());
        if (clienteEncontrado != null && clienteEncontrado.getContra().equals(cliente.getContra())) {
            return "inicio-cliente"; // Página de inicio para el cliente
        } else {
            model.addAttribute("mensaje", "Cédula o contraseña incorrectos");
            return "login-cliente"; // Redirigir al formulario de login si no coincide
        }
    }

    // Mostrar la página de login para Administradores
    @GetMapping("/administrador")
    public String mostrarLoginAdministrador() {
        return "login-administrador"; // Página de login para administradores
    }

    // Procesar el login de Administrador
    @PostMapping("/administrador")
    public String loginAdministrador(@ModelAttribute Administrador administrador, Model model) {
        Administrador existsAdministrador = administradorRepo.findByCedula(administrador.getCedula());
        if (existsAdministrador != null && existsAdministrador.getContra().equals(administrador.getContra())) {
            model.addAttribute("administrador", administradorRepo.findAll());
            return "inicio-administrador"; // Página de inicio para el administrador
        } else {
            model.addAttribute("mensaje", "Nombre de usuario o contraseña incorrectos");
            return "login-administrador"; // Redirigir al formulario de login si no coincide
        }
    }

    // Página de inicio para Administrador (ya estaba en AdminWebController)
    @GetMapping("/administrador/inicio")
    public String mostrarInicioAdministrador(Model model) {
        model.addAttribute("administrador", administradorRepo.findAll());
        return "inicio-administrador"; // Página de inicio para el administrador
    }

    // Página de inicio para Cliente (similar al inicio del administrador)
    @GetMapping("/cliente/inicio")
    public String mostrarInicioCliente() {
        return "/articulos/cliente/catalogo"; // Página de inicio para el cliente
    }

    // Formulario de registro para el Administrador (para agregar un nuevo administrador)
    @GetMapping("/administrador/registro")
    public String registroAdministradorFormulario(Model model) {
        model.addAttribute("administrador", new Administrador());
        return "registro-administrador";
    }

    // Guardar un nuevo Administrador
    @PostMapping("/administrador/registro")
    public String registrarAdministrador(@ModelAttribute Administrador administrador) {
        administradorRepo.save(administrador);
        return "redirect:/login/administrador"; // Redirige a la página de login para administradores
    }

    // Formulario de registro para el Cliente
    @GetMapping("/cliente/registro")
    public String registroClienteFormulario(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "registro-cliente"; // Página de registro de cliente
    }

    // Guardar un nuevo Cliente
    @PostMapping("/cliente/registro")
    public String registrarCliente(@ModelAttribute Cliente cliente) {
        clienteRepo.save(cliente);
        return "redirect:/login/cliente"; // Redirige a la página de login para clientes
    }
}
