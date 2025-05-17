package com.Distribuidora.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Distribuidora.app.model.Administrador;
import com.Distribuidora.app.model.Bodega;
import com.Distribuidora.app.model.Cliente;
import com.Distribuidora.app.model.Repartidor;
import com.Distribuidora.app.model.Vendedor;
import com.Distribuidora.app.repository.AdministradorRepository;
import com.Distribuidora.app.repository.BodegaRepository;
import com.Distribuidora.app.repository.ClienteRepository;
import com.Distribuidora.app.repository.RepartidorRepository;
import com.Distribuidora.app.repository.VendedorRepository;

@Controller
@RequestMapping("administrador")
public class AdminWebController {
	   @Autowired
	    private AdministradorRepository administradorRepo;
	   @Autowired
	   private BodegaRepository bodegaRepo;

	   @Autowired
	   private RepartidorRepository repartidorRepo;

	   @Autowired
	   private VendedorRepository vendedorRepo;
	   @Autowired
	   private ClienteRepository clienteRepo;

	    // Formulario para agregar un nuevo administrador
	    @GetMapping("/agregar")
	    public String agregarAdministradorFormulario(Model model) {
	        model.addAttribute("administrador", new Administrador());
	        return "registro-administrador";
	    }

	    // Guardar un nuevo administrador en la base de datos
	    @PostMapping("/guardar")
	    public String guardarAdministrador(@ModelAttribute Administrador administrador) {
	        administradorRepo.save(administrador);
	        return "redirect:/administrador/inicio";
	    }

	    // Formulario para editar un administrador existente
	    @GetMapping("/editar/{cedula}")
	    public String editarAdministradorFormulario(@PathVariable String cedula, Model model) {
	        Administrador administrador = administradorRepo.findByCedula(cedula);
	        if (administrador != null) {
	            model.addAttribute("administrador", administrador);
	            return "registro-administrador";
	        } else {
	            return "redirect:/administrador/inicio";
	        }
	    }

	    // Borrar un administrador por su cédula
	    @GetMapping("/borrar/{cedula}")
	    public String borrarAdministradorFormulario(@PathVariable String cedula, Model model) {
	        administradorRepo.deleteById(cedula);
	        return "redirect:/administrador/inicio";
	    }
	    
	    @GetMapping("/")
		public String mostrarLogin() {
			return "login-administrador";
	    }
	    @GetMapping("/inicio")
		public String mostrarInicio(Model model) {
			model.addAttribute("administrador", administradorRepo.findAll());
				return "inicio-administrador";
	    }
	    
	    @PostMapping("/login")
	    public String loginFormulario(String cedula, String contra, String tipoUsuario, Model model) {
	        switch (tipoUsuario) {
	            case "administrador":
	                Administrador administrador = administradorRepo.findByCedula(cedula);
	                if (administrador != null && administrador.getContra().equals(contra)) {
	                    return "inicio-administrador";
	                }
	                break;
	            case "cliente":
	                Cliente cliente = clienteRepo.findByCedula(cedula);
	                if (cliente != null && cliente.getContra().equals(contra)) {
	                	return "redirect:/articulos/cliente/catalogo";

	                }
	                break;
	            case "bodega":
	                Bodega bodega = bodegaRepo.findByCedula(cedula);
	                if (bodega != null && bodega.getContra().equals(contra)) {
	                    return "indexBodega";
	                }
	                break;
	            case "repartidor":
	                Repartidor repartidor = repartidorRepo.findByCedula(cedula);
	                if (repartidor != null && repartidor.getContrasena().equals(contra)) {
	                    return "indexRepartidor";
	                }
	                break;
	            case "vendedor":
	                Vendedor vendedor = vendedorRepo.findByCedula(cedula);
	                if (vendedor != null && vendedor.getContrasena().equals(contra)) {
	                    return "redirect:/articulos/cliente/catalogoV";
	                }
	                break;
	        }

	        model.addAttribute("mensaje", "Usuario o contraseña incorrectos");
	        return "login-administrador"; // o el nombre de tu login
	    }

	    
	    @GetMapping("/registro")
	    public String registroFormulario(Model model) {
	        model.addAttribute("cliente", new Cliente());
	        return "registro-administrador";
	    }

	    @PostMapping("/registro")
	    public String registrarCliente(@ModelAttribute Cliente cliente) {
	        clienteRepo.save(cliente); // Asegúrate de tener clienteRepo correctamente inyectado
	        return "redirect:/cliente/";
	    }
	    
	    
	    
	    
	    
	    
	}

